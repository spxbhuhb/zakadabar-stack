/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.content.backend

import kotlinx.datetime.Clock
import zakadabar.lib.content.data.*
import zakadabar.lib.i18n.backend.LocaleBl
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.module
import zakadabar.stack.data.entity.EntityId

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
    private val stereotypeBl by module<StereotypeBl>()

    override val router = router {
        query(ContentOverviewQuery::class, ::overview)
        query(ContentByStereotypeAndTitle::class, ::byStereotypeAndTitle)
    }

    override fun create(executor: Executor, bo: ContentBo): ContentBo {
        bo.modifiedBy = EntityId(executor.accountId)
        bo.modifiedAt = Clock.System.now()
        return super.create(executor, bo)
    }

    override fun update(executor: Executor, bo: ContentBo): ContentBo {
        bo.modifiedBy = EntityId(executor.accountId)
        bo.modifiedAt = Clock.System.now()
        return super.update(executor, bo)
    }

    @Suppress("UNUSED_PARAMETER") // this is fine, needed for routing
    private fun overview(executor: Executor, query: ContentOverviewQuery): ContentOverview {

        val entries = mutableListOf<ContentOverviewEntry>()

        val locales = localeBl.list(executor)
        val statuses = statusBl.list(executor).sortedBy { it.id }
        val categories = stereotypeBl.list(executor).sortedBy { it.id }

        val bos = pa.list()

        // make an entry for each Master

        bos.forEach { bo ->
            if (bo.master != null) return@forEach

            entries += ContentOverviewEntry(
                bo.id,
                bo.title,
                categories.first { it.id == bo.stereotype }.name,
                statuses.first { it.id == bo.status }.name,
                localizations = MutableList(locales.size) { null }
            )

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
    private fun byStereotypeAndTitle(executor: Executor, query: ContentByStereotypeAndTitle): ContentBo {
        val locale = localeBl.byName(query.localeName) ?: throw NoSuchElementException()
        val stereotypeId = stereotypeBl.getId(locale.id, query.localizedStereotypeName)

        return pa.byLocalizedTitle(stereotypeId, query.localizedContentTitle)
    }

}