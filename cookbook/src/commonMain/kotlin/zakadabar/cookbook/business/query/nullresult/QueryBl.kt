/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.query.nullresult

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.StringValue
import zakadabar.stack.util.PublicApi

@PublicApi
class QueryBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = Query.boNamespace

    override val router = router {
        query(Query::class, ::query)
    }

    override val authorizer by provider()

    fun query(executor: Executor, bo: Query): StringValue? =
        bo.returnValue?.let { StringValue(it) }

}