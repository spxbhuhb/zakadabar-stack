/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.query.nullresult

import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.QueryBusinessLogicBase
import zakadabar.core.data.BaseBo
import zakadabar.core.data.builtin.StringValue
import zakadabar.core.data.query.QueryBo
import zakadabar.core.util.PublicApi

@PublicApi
class QueryBl : QueryBusinessLogicBase<Query, StringValue?>(
    queryBoClass = Query::class
) {

    override val authorizer = object : BusinessLogicAuthorizer<BaseBo> {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            return
        }
    }

    override fun execute(executor: Executor, bo: Query): StringValue? =
        bo.returnValue?.let { StringValue(it) }

}