/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import zakadabar.lib.examples.data.SimpleQueryResult
import zakadabar.lib.examples.data.SimpleStandaloneQuery
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.business.QueryBusinessLogicBase
import zakadabar.stack.data.entity.EmptyEntityBo

class SimpleStandaloneQueryBl : QueryBusinessLogicBase<SimpleStandaloneQuery, List<SimpleQueryResult>>(
    queryBoClass = SimpleStandaloneQuery::class
) {

    override val authorizer = SimpleRoleAuthorizer<EmptyEntityBo> {
        query(SimpleStandaloneQuery::class, StackRoles.siteMember)
    }

    override fun execute(executor: Executor, bo: SimpleStandaloneQuery): List<SimpleQueryResult> {
        return emptyList()
    }

}