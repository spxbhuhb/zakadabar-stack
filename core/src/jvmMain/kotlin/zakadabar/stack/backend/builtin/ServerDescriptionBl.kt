/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin

import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.backend.authorize.EmptyAuthorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.QueryBusinessLogicBase
import zakadabar.stack.backend.server
import zakadabar.stack.data.builtin.misc.ServerDescriptionBo
import zakadabar.stack.data.builtin.misc.ServerDescriptionQuery
import zakadabar.stack.data.entity.EmptyEntityBo
import zakadabar.stack.data.query.QueryBo

class ServerDescriptionBl : QueryBusinessLogicBase<ServerDescriptionQuery, ServerDescriptionBo>(
    queryBoClass = ServerDescriptionQuery::class
), RoutedModule {

    override val authorizer = object : EmptyAuthorizer<EmptyEntityBo>() {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            return
        }
    }

    override fun execute(executor: Executor, bo: ServerDescriptionQuery): ServerDescriptionBo =
        server.description

}
