/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.action.loggedin

import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.authorize
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.data.ActionBo
import zakadabar.core.data.LongValue
import zakadabar.core.exception.Unauthorized
import zakadabar.core.util.PublicApi

@PublicApi
class ActionBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = Action.boNamespace

    override val router = router {
        action(Action::class, ::action)
    }

    override val authorizer = object : BusinessLogicAuthorizer<BaseBo> {
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