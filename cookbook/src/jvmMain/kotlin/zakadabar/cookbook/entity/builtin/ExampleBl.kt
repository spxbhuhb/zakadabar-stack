/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.entity.builtin

import zakadabar.cookbook.browser.table.query.ExampleQuery
import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase

open class ExampleBl : EntityBusinessLogicBase<ExampleBo>(
    boClass = ExampleBo::class
) {

    override val pa = ExamplePa()

    override val authorizer by provider()

    override val router = router {
        query(ExampleQuery::class, ::exampleQuery)
    }

    override fun create(executor: Executor, bo: ExampleBo): ExampleBo {
        if (pa.count() >= 1000) throw IllegalStateException("table limit reached")
        return pa.create(bo)
    }

    fun exampleQuery(executor: Executor, query: ExampleQuery): List<ExampleBo> {
        return pa.list()
    }

}