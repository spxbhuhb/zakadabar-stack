/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.query.anonymous

import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.exceptions.Unauthorized
import zakadabar.stack.util.PublicApi

@PublicApi
class QueryBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = Query.boNamespace

    override val router = router {
        query(Query::class, ::query)
    }

    override val authorizer = object : Authorizer<BaseBo> {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            if (queryBo is Query) return
            throw Unauthorized()
        }
    }

    fun query(executor: Executor, bo: Query): List<String> =
        listOf("Hello", "World")

}