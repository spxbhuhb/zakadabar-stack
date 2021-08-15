/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.query.anonymous

import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.util.PublicApi

@PublicApi
class QuerySraBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = Query.boNamespace

    override val router = router {
        query(Query::class, ::query)
    }

    override val authorizer by provider {
        this as SimpleRoleAuthorizer
        query(Query::class, PUBLIC)
    }

    fun query(executor: Executor, bo: Query): List<String> =
        listOf("Hello", "World")

}