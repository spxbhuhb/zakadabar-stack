/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import zakadabar.lib.examples.data.SimpleQueryResult
import zakadabar.lib.examples.data.SimpleStandaloneQuery
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.business.QueryBusinessLogicBase
import zakadabar.stack.data.BaseBo

class SimpleStandaloneQueryBl : QueryBusinessLogicBase<SimpleStandaloneQuery, List<SimpleQueryResult>>(
    queryBoClass = SimpleStandaloneQuery::class
) {

    override val authorizer = SimpleRoleAuthorizer<BaseBo> {
        query(SimpleStandaloneQuery::class, LOGGED_IN)
    }

    override fun execute(executor: Executor, bo: SimpleStandaloneQuery): List<SimpleQueryResult> {
        return emptyList()
    }

}