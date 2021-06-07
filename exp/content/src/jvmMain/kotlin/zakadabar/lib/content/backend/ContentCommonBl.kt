/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import kotlinx.datetime.Clock
import zakadabar.lib.content.data.ContentCommonBo
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.data.entity.EntityId

/**
 * Business Logic for ContentBo.
 * 
 * Generated with Bender at 2021-06-05T06:12:00.490Z.
 */
open class ContentCommonBl : EntityBusinessLogicBase<ContentCommonBo>(
    boClass = ContentCommonBo::class
) {

    override val pa = ContentExposedPaGen()

    override val authorizer by provider()

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

}