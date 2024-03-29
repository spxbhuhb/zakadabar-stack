/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.content.backend

import io.ktor.features.*
import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.EntityId
import zakadabar.core.data.StringValue
import zakadabar.core.exception.DataConflict
import zakadabar.core.module.module
import zakadabar.core.text.lowercaseWithHyphen
import zakadabar.core.util.PublicApi
import zakadabar.lib.blobs.data.url
import zakadabar.lib.content.data.*
import zakadabar.lib.i18n.business.LocaleBl
import zakadabar.lib.i18n.data.LocaleBo

/**
 * Business Logic for ContentBo.
 */
open class ContentBl : EntityBusinessLogicBase<ContentBo>(
    boClass = ContentBo::class
) {

    override val pa = ContentPa()

    override val authorizer by provider()

    private val localeBl by module<LocaleBl>()
    private val statusBl by module<StatusBl>()
    private val blobBl by module<AttachedBlobBl>()

    override val router = router {
        query(ContentOverviewQuery::class, ::overview)
        query(BySeoPath::class, ::bySeoPath)
        query(MastersQuery::class, ::mastersQuery)
        query(FolderQuery::class, ::foldersQuery)
        query(NavQuery::class, ::navQuery)
        query(ThumbnailQuery::class, ::thumbnailQuery)
        query(LocaleChangeQuery::class, ::localeChangeQuery)
        query(LocaleOptionsQuery::class, ::localeOptionsQuery)
    }

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    /**
     * Verifies that there is no document under the same parent with the
     * same locale and same SEO title.
     *
     * @throws  DataConflict   if there is a title conflict
     */
    private fun checkConsistency(bo: ContentBo) {
        val master = bo.master?.let { pa.read(it) } ?: return // masters does not have to obey collision
        val locale = bo.locale ?: throw IllegalStateException("localized content without locale")

        val localized = pa.bySeoTitle(locale, master.parent, bo.seoTitle) ?: return // no collision

        if (localized.id == bo.id) return // this is an update

        throw DataConflict("SEO title conflict")
    }

    override fun list(executor: Executor): List<ContentBo> {
        throw NotImplementedError("direct content listing is not implemented, use queries")
    }

    override fun create(executor: Executor, bo: ContentBo): ContentBo {
        bo.seoTitle = bo.title.lowercaseWithHyphen()

        checkConsistency(bo)

        return super.create(executor, bo)
    }

    override fun read(executor: Executor, entityId: EntityId<ContentBo>): ContentBo {
        val bo = super.read(executor, entityId)

        val master = bo.master

        bo.attachments = if (master == null) {
            blobBl.byReference(bo.id)
        } else {
            findImages(null, master, bo.id)
        }

        return bo
    }

    override fun update(executor: Executor, bo: ContentBo): ContentBo {
        bo.seoTitle = bo.title.lowercaseWithHyphen()

        // check that there is no loop
        var current = bo.parent
        while (current != null) {
            if (current == bo.id) throw BadRequestException("invalid parent: loop in path")
            current = pa.read(current).parent
        }

        return super.update(executor, bo)
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

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
    fun bySeoPath(executor: Executor, query: BySeoPath): ContentBo = bySeoPath(query.locale, query.path)

    fun bySeoPath(localeName: String, path: String, returnWithMaster: Boolean = false): ContentBo {
        val segments = path.trim('/').split("/")

        val locale = localeBl.byName(localeName)?.id ?: throw NoSuchElementException()

        var localized = pa.bySeoTitle(locale, null, segments[0]) ?: throw NoSuchElementException()
        var master = pa.read(localized.master !!)

        for (i in 1 until segments.size) {
            localized = pa.bySeoTitle(locale, master.id, segments[i]) ?: throw NoSuchElementException()
            master = pa.read(localized.master !!)
        }

        if (returnWithMaster) return master

        localized.attachments = findImages(null, master, localized)

        return localized
    }

    fun seoPath(bo: ContentBo) = seoPathOrNull(bo) ?: throw NoSuchElementException()

    fun seoPathOrNull(bo: ContentBo): String? {
        val locale = bo.locale
            ?: throw IllegalArgumentException("localized BO ${bo.id} locale is null, cannot find SEO path")

        val seoPath = mutableListOf(bo.seoTitle)

        var localized = bo
        var master = localized.master?.let { pa.read(it) }
            ?: throw IllegalArgumentException("localized BO ${localized.id} master is null")

        while (master.parent != null) {
            master = pa.read(master.parent !!)
            localized = pa.readLocalizedOrNull(master.id, locale) ?: return null
            seoPath += localized.seoTitle
        }

        seoPath.reverse()

        return seoPath.joinToString("/")
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mastersQuery(executor: Executor, query: MastersQuery): List<MastersEntry> =
        pa.mastersQuery()

    @Suppress("UNUSED_PARAMETER")
    private fun foldersQuery(executor: Executor, query: FolderQuery): List<FolderEntry> =
        pa.folderQuery()

    @Suppress("UNUSED_PARAMETER")
    @PublicApi
    fun navQuery(executor: Executor, query: NavQuery): List<NavEntry> {
        val locale = localeBl.byName(query.locale) ?: throw NoSuchElementException()

        if (query.from == null) {
            return pa.navQuery(locale.id, null, "/${locale.name}")
        }

        val from = pa.read(query.from)

        val master = from.master?.let { pa.read(it) } ?: from
        val localized = if (from.master == null) pa.readLocalized(master.id, locale.id) else from

        return pa.navQuery(locale.id, master.id, "/${locale.name}/${seoPath(localized)}")
    }

    @Suppress("UNUSED_PARAMETER")
    @PublicApi
    fun thumbnailQuery(executor: Executor, query: ThumbnailQuery): List<ThumbnailEntry> {
        val locale = localeBl.byName(query.locale) ?: throw NoSuchElementException()

        val (master, localized) = masterAndLocalized(locale.id, query.parent)

        val list = pa.thumbnailQuery(locale.id, master.id, "/${locale.name}/${seoPath(localized)}")

        list.forEach {
            it.thumbnailImageUrl = findImages(AttachedBlobDisposition.thumbnail, it.masterId, it.localizedId).firstOrNull()?.url
        }

        return list
    }

    @Suppress("UNUSED_PARAMETER")
    @PublicApi
    fun localeChangeQuery(executor: Executor, query: LocaleChangeQuery): StringValue {
        val locale = localeBl.byName(query.toLocale) ?: throw NoSuchElementException()

        val master = bySeoPath(query.fromLocale, query.fromPath, returnWithMaster = true)
        val localized = pa.readLocalizedOrNull(master.id, locale.id) ?: throw NoSuchElementException()

        return StringValue(seoPath(localized))
    }

    @Suppress("UNUSED_PARAMETER")
    @PublicApi
    fun localeOptionsQuery(executor: Executor, query: LocaleOptionsQuery): List<LocaleAndPath> {
        val locales = localeBl.list(executor)

        val master = bySeoPath(query.fromLocale, query.fromPath, returnWithMaster = true)

        return pa
            .listLocalized(master.id)
            .map { bo ->
                LocaleAndPath(locales.first { it.id == bo.locale }.name, seoPath(bo))
            }
    }

    // -------------------------------------------------------------------------
    // Functions for other business logic modules
    // -------------------------------------------------------------------------

    /**
     * Get the SEO path for the given locale and master id.
     *
     * @param   localeId  Id of the locale to get the SEO path version for.
     * @param   masterId  Id of the master content.
     *
     * @return  the seo path or null if it cannot be built because a localized version is missing
     */
    @PublicApi
    fun seoPathOrNull(localeId: EntityId<LocaleBo>, masterId: EntityId<ContentBo>) =
        localizedOrNull(localeId, masterId)?.let { seoPathOrNull(it) }

    /**
     * Get the SEO path for the given locale and master id.
     *
     * @param   localeId  Id of the locale to get the SEO path version for.
     * @param   masterId  Id of the master content.
     *
     * @throws  NoSuchElementException  when the locale is unknown
     *                                  when there is no localized version
     */
    @PublicApi
    fun seoPath(localeId: EntityId<LocaleBo>, masterId: EntityId<ContentBo>) =
        seoPath(localized(localeId, masterId))

    /**
     * Read the  localized version of the content based on the
     * locale of the executor and the id of the master.
     *
     * @param   localeId  Id of the locale to get the localized version for.
     * @param   masterId  Id of the master content.
     *
     * @throws  NoSuchElementException  when the locale is unknown
     *                                  when there is no localized version
     */
    @PublicApi
    fun localized(localeId: EntityId<LocaleBo>, masterId: EntityId<ContentBo>) =
        pa.readLocalized(masterId, localeId)

    /**
     * Read the  localized version of the content based on the
     * locale of the executor and the id of the master.
     *
     * @param   localeId  Id of the locale to get the localized version for.
     * @param   masterId  Id of the master content.
     *
     * @return  the localized version or null if no localized version found
     */
    @PublicApi
    fun localizedOrNull(localeId: EntityId<LocaleBo>, masterId: EntityId<ContentBo>) =
        pa.readLocalizedOrNull(masterId, localeId)

    /**
     * Read the master and the localized versions of the content.
     *
     * @param   localeId  Id of the locale to get the localized version for.
     * @param   entityId  Id of the content to read. May be the master or the
     *                    localized version, the function sorts it out.
     *
     * @throws  NoSuchElementException  when the locale is unknown
     *                                  when there is no master version
     *                                  when there is no localized version
     */
    @PublicApi
    fun masterAndLocalized(localeId: EntityId<LocaleBo>, entityId: EntityId<ContentBo>): Pair<ContentBo, ContentBo> {
        val base = pa.read(entityId)

        val master = base.master?.let { pa.read(it) } ?: base
        val localized = base.locale?.let { base } ?: pa.readLocalized(master.id, localeId)

        return master to localized
    }

    /**
     * Find images. Handles disposition and localization.
     *
     * @param   disposition   The disposition to filter images for.
     * @param   master        The master content BO to get images for.
     * @param   localized     The localized content Bo to get images for.
     *
     * @return  List of images for the disposition. When there is at least one localized
     *          image for the disposition, only the localized images are in the list.
     *          When there is no localized image, master images are in the list.
     */
    @PublicApi
    fun findImages(disposition: String?, master: ContentBo, localized: ContentBo) =
        findImages(disposition, master.id, localized.id)

    /**
     * Find images. Handles disposition and localization.
     *
     * @param   disposition     The disposition to filter images for.
     * @param   masterId        The id of the master content BO to get images for.
     * @param   localizedId     The id of the localized content Bo to get images for.
     *
     * @return  List of images for the disposition. When there is at least one localized
     *          image for the disposition, only the localized images are in the list.
     *          When there is no localized image, master images are in the list.
     */
    @PublicApi
    fun findImages(disposition: String?, masterId: EntityId<ContentBo>, localizedId: EntityId<ContentBo>): List<AttachedBlobBo> {
        val list = blobBl.byReference(localizedId, disposition)
        if (list.isNotEmpty()) return list
        return blobBl.byReference(masterId)
    }
}