/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.action.loggedin

import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.data.LongValue
import zakadabar.core.util.PublicApi

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