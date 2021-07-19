/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.builtin

import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase

open class BuiltinBl : EntityBusinessLogicBase<BuiltinBo>(
    boClass = BuiltinBo::class
) {

    override val pa = BuiltinExposedPa()

    override val authorizer by provider()

    override fun create(executor: Executor, bo: BuiltinBo): BuiltinBo {
        if (pa.count() >= 1000) throw IllegalStateException("table limit reached")
        return pa.create(bo)
    }

}