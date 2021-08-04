/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin

import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.QueryBusinessLogicBase
import zakadabar.stack.backend.server
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.misc.ServerDescriptionBo
import zakadabar.stack.data.builtin.misc.ServerDescriptionQuery
import zakadabar.stack.data.query.QueryBo

class ServerDescriptionBl : QueryBusinessLogicBase<ServerDescriptionQuery, ServerDescriptionBo>(
    queryBoClass = ServerDescriptionQuery::class
) {

    override val authorizer = object : Authorizer<BaseBo> {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            return
        }
    }

    override fun execute(executor: Executor, bo: ServerDescriptionQuery): ServerDescriptionBo =
        server.description

}
