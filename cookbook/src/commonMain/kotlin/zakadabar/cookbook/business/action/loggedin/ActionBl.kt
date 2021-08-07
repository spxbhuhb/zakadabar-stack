/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.action.loggedin

import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.authorize
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.builtin.LongValue
import zakadabar.stack.exceptions.Unauthorized
import zakadabar.stack.util.PublicApi

@PublicApi
class ActionBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = Action.boNamespace

    override val router = router {
        action(Action::class, ::action)
    }

    override val authorizer = object : Authorizer<BaseBo> {
        override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
            if (actionBo is Action) {
                authorize(executor.isLoggedIn)
                return
            }
            throw Unauthorized()
        }
    }

    fun action(executor: Executor, bo: Action): LongValue = LongValue(43)

}