/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.data.QueryBo

class ServerDescriptionBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = ServerDescriptionQuery.boNamespace

    override val router = router {
        query(ServerDescriptionQuery::class, ::serverDescriptionQuery)
    }

    override val authorizer = object : BusinessLogicAuthorizer<BaseBo> {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            return
        }
    }

    fun serverDescriptionQuery(executor: Executor, bo: ServerDescriptionQuery): ServerDescriptionBo =
        server.description

}
