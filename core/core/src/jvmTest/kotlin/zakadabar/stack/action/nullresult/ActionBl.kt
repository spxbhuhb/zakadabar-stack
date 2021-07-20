/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.action.nullresult

import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.ActionBusinessLogicBase
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.builtin.StringValue
import zakadabar.stack.util.PublicApi

@PublicApi
class ActionBl : ActionBusinessLogicBase<Action, StringValue?>(
    actionBoClass = Action::class
) {

    override val authorizer = object : Authorizer<BaseBo> {
        override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
            return
        }
    }

    override fun execute(executor: Executor, bo: Action): StringValue? =
        bo.returnValue?.let { StringValue(it) }

}