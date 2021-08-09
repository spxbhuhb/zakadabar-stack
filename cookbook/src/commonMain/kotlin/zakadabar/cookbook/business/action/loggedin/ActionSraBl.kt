/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.action.loggedin

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.LongValue
import zakadabar.stack.util.PublicApi

@PublicApi
class ActionSraBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = Action.boNamespace

    override val router = router {
        action(Action::class, ::action)
    }

    override val authorizer by provider {
        this as SimpleRoleAuthorizer
        action(Action::class, LOGGED_IN)
    }

    fun action(executor: Executor, bo: Action): LongValue = LongValue(43)

}