/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.content.backend

import kotlinx.datetime.Clock
import zakadabar.lib.content.data.ContentCommonBo
import zakadabar.lib.content.data.Overview
import zakadabar.lib.content.data.OverviewEntry
import zakadabar.lib.content.data.OverviewQuery
import zakadabar.lib.i18n.backend.LocaleBl
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.module
import zakadabar.stack.data.entity.EntityId

/**
 * Business Logic for ContentCommonBo.
 */
open class ContentCommonBl : EntityBusinessLogicBase<ContentCommonBo>(
    boClass = ContentCommonBo::class
) {

    override val pa = ContentExposedPaGen()

    override val authorizer by provider()

    private val localeBl by module<LocaleBl>()
    private val contentStatusBl by module<ContentStatusBl>()
    private val contentCategoryBl by module<ContentCategoryBl>()

    override val router = router {
        query(OverviewQuery::class, ::overview)
    }

    override fun create(executor: Executor, bo: ContentCommonBo): ContentCommonBo {
        bo.modifiedBy = EntityId(executor.accountId)
        bo.modifiedAt = Clock.System.now()
        return super.create(executor, bo)
    }

    override fun update(executor: Executor, bo: ContentCommonBo): ContentCommonBo {
        bo.modifiedBy = EntityId(executor.accountId)
        bo.modifiedAt = Clock.System.now()
        return super.update(executor, bo)
    }

    @Suppress("UNUSED_PARAMETER") // this is fine, needed for routing
    private fun overview(executor: Executor, query: OverviewQuery): Overview {

        val entries = mutableListOf<OverviewEntry>()

        val locales = localeBl.list(executor)
        val statuses = contentStatusBl.list(executor).sortedBy { it.id }
        val categories = contentCategoryBl.list(executor).sortedBy { it.id }

        val bos = pa.list()

        // make an entry for each Master

        bos.forEach { bo ->
            if (bo.master != null) return@forEach

            entries += OverviewEntry(
                bo.id,
                bo.title,
                categories.first { it.id == bo.category }.name,
                statuses.first { it.id == bo.status }.name,
                locales = MutableList(locales.size) { null }
            )

        }

        // add each Non-Master to it's Master

        bos.forEach { bo ->
            if (bo.master == null) return@forEach

            entries.first { it.id == bo.master }.also { entry ->
                val index = locales.indexOfFirst { it.id == bo.locale }
                (entry.locales as MutableList)[index] = bo.id
            }
        }

        return Overview(
            locales = locales,
            entries = entries
        )
    }

}