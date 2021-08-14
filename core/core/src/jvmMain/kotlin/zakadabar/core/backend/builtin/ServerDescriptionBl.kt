/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.builtin

import zakadabar.core.backend.authorize.BusinessLogicAuthorizer
import zakadabar.core.backend.authorize.Executor
import zakadabar.core.backend.business.QueryBusinessLogicBase
import zakadabar.core.backend.server
import zakadabar.core.data.BaseBo
import zakadabar.core.data.builtin.misc.ServerDescriptionBo
import zakadabar.core.data.builtin.misc.ServerDescriptionQuery
import zakadabar.core.data.query.QueryBo

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
