/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.QueryBusinessLogicBase
import zakadabar.core.data.BaseBo
import zakadabar.core.data.QueryBo

class ServerDescriptionBl : QueryBusinessLogicBase<ServerDescriptionQuery, ServerDescriptionBo>(
    queryBoClass = ServerDescriptionQuery::class
) {

    override val authorizer = object : BusinessLogicAuthorizer<BaseBo> {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            return
        }
    }

    override fun execute(executor: Executor, bo: ServerDescriptionQuery): ServerDescriptionBo =
        server.description

}
