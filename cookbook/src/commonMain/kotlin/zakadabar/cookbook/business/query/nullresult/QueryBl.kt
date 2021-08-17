/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.query.nullresult

import zakadabar.core.authorize.Executor
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.data.StringValue
import zakadabar.core.util.PublicApi

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