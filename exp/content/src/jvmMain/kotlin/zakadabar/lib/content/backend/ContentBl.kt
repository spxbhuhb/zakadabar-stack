/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.content.backend

import io.ktor.features.*
import kotlinx.datetime.Clock
import zakadabar.lib.content.data.*
import zakadabar.lib.i18n.backend.LocaleBl
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.module
import zakadabar.stack.data.DataConflictException
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.text.lowercaseWithHyphen

/**
 * Business Logic for ContentCommonBo.
 */
open class ContentBl : EntityBusinessLogicBase<ContentBo>(
    boClass = ContentBo::class
) {

    override val pa = ContentExposedPa()

    override val authorizer by provider()

    private val localeBl by module<LocaleBl>()
    private val statusBl by module<StatusBl>()

    override val router = router {
        query(ContentOverviewQuery::class, ::overview)
        query(BySeoPath::class, ::bySeoPath)
        query(FolderQuery::class, ::mastersQuery)
        query(NavQuery::class, ::navQuery)
    }

    override fun create(executor: Executor, bo: ContentBo): ContentBo {
        bo.modifiedBy = EntityId(executor.accountId)
        bo.modifiedAt = Clock.System.now()
        bo.seoTitle = bo.title.lowercaseWithHyphen()

        checkConsistency(bo)

        return super.create(executor, bo)
    }

    /**
     * Verifies that there is no document under the same parent with the
     * same locale and same SEO title.
     *
     * @throws  DataConflictException   if there is a title conflict
     */
    private fun checkConsistency(bo: ContentBo) {
        val master = bo.master?.let { pa.read(it) } ?: return // masters does not have to obey collision
        val locale = bo.locale ?: throw IllegalStateException("localized content without locale")

        val localized = pa.bySeoTitle(locale, master.parent, bo.seoTitle) ?: return // no collision

        if (localized.id == bo.id) return // this is an update

        throw DataConflictException("SEO title conflict")
    }

    override fun update(executor: Executor, bo: ContentBo): ContentBo {
        bo.modifiedBy = EntityId(executor.accountId)
        bo.modifiedAt = Clock.System.now()
        bo.seoTitle = bo.title.lowercaseWithHyphen()

        // check that there is no loop
        var current = bo.parent
        while (current != null) {
            if (current == bo.id) throw BadRequestException("invalid parent: loop in path")
            current = pa.read(current).parent
        }

        return super.update(executor, bo)
    }

    @Suppress("UNUSED_PARAMETER") // this is fine, needed for routing
    private fun overview(executor: Executor, query: ContentOverviewQuery): ContentOverview {

        val entries = mutableListOf<ContentOverviewEntry>()

        val locales = localeBl.list(executor)
        val statuses = statusBl.list(executor).sortedBy { it.id }

        val bos = pa.list()
        val map = bos.associateBy { it.id }

        // make an entry for each Master

        bos.forEach { bo ->
            if (bo.master != null) return@forEach

            entries += ContentOverviewEntry(
                bo.id,
                parent = bo.parent,
                path = bo.title,
                status = statuses.first { it.id == bo.status }.name,
                localizations = MutableList(locales.size) { null }
            )

        }

        // now we have all masters collected, time to build paths

        entries.forEach {
            val path = mutableListOf(it.path)
            var cid = it.parent
            while (cid != null) {
                val parent = map[cid] ?: throw IllegalStateException("missing content parent: ${it.parent}")
                path += parent.title
                cid = parent.parent
            }
            path.reverse()
            it.path = path.joinToString(" / ")
        }

        // add each Non-Master to it's Master

        bos.forEach { bo ->
            if (bo.master == null) return@forEach

            entries.first { it.id == bo.master }.also { entry ->
                val index = locales.indexOfFirst { it.id == bo.locale }
                (entry.localizations as MutableList)[index] = bo.id
            }
        }

        return ContentOverview(
            locales = locales,
            entries = entries
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun bySeoPath(executor: Executor, query: BySeoPath): ContentBo {
        val segments = query.path.trim('/').split("/")
        if (segments.size < 2) throw NoSuchElementException()

        val locale = localeBl.byName(segments[0])?.id ?: throw NoSuchElementException()

        var localized = pa.bySeoTitle(locale, null, segments[1]) ?: throw NoSuchElementException()
        var master = pa.read(localized.master !!)

        for (i in 2 until segments.size) {
            localized = pa.bySeoTitle(locale, master.id, segments[i]) ?: throw NoSuchElementException()
            master = pa.read(localized.master !!)
        }

        return localized
    }

    private fun seoPath(bo: ContentBo): List<String> {
        val locale = bo.locale
            ?: throw IllegalArgumentException("localized BO ${bo.id} locale is null, cannot find SEO path")

        val seoPath = mutableListOf(bo.seoTitle)

        var localized = bo
        var master = localized.master?.let { pa.read(it) }
            ?: throw IllegalArgumentException("localized BO ${localized.id} master is null")

        while (master.parent != null) {
            master = pa.read(master.parent !!)
            localized = pa.readLocalized(master.id, locale)
            seoPath += localized.seoTitle
        }

        seoPath.reverse()

        return seoPath
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mastersQuery(executor: Executor, folderQuery: FolderQuery): List<FolderEntry> =
        pa.folderQuery()


    @Suppress("UNUSED_PARAMETER")
    private fun navQuery(executor: Executor, query: NavQuery): List<NavEntry> {
        val locale = localeBl.byName(query.localeName) ?: throw NoSuchElementException()

        if (query.from == null) {
            return pa.navQuery(locale.id, null, "/${locale.name}")
        }

        val from = pa.read(query.from)

        val master = from.master?.let { pa.read(it) } ?: from
        val localized = if (from.master == null) pa.readLocalized(master.id, locale.id) else from

        return pa.navQuery(locale.id, master.id, "/${locale.name}/${seoPath(localized).joinToString("/")}")
    }

}