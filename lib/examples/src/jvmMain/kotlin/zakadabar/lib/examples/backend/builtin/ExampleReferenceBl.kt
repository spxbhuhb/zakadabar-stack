/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.builtin

import zakadabar.lib.examples.data.builtin.ExampleReferenceBo
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase

open class ExampleReferenceBl : EntityBusinessLogicBase<ExampleReferenceBo>(
    boClass = ExampleReferenceBo::class
) {

    override val pa = ExampleReferenceExposedPa()

    override val authorizer by provider()

    override fun create(executor: Executor, bo: ExampleReferenceBo): ExampleReferenceBo {
        if (pa.count() >= 1000) throw IllegalStateException("table limit reached")
        return pa.create(bo)
    }

}