/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.entity.builtin

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase

open class ExampleBl : EntityBusinessLogicBase<ExampleBo>(
    boClass = ExampleBo::class
) {

    override val pa = ExamplePa()

    override val authorizer by provider()

    override fun create(executor: Executor, bo: ExampleBo): ExampleBo {
        if (pa.count() >= 1000) throw IllegalStateException("table limit reached")
        return pa.create(bo)
    }

}