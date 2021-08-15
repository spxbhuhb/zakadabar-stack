/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.action.nullresult

import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.ActionBusinessLogicBase
import zakadabar.core.data.BaseBo
import zakadabar.core.data.ActionBo
import zakadabar.core.data.StringValue
import zakadabar.core.util.PublicApi

@PublicApi
class ActionBl : ActionBusinessLogicBase<Action, StringValue?>(
    actionBoClass = Action::class
) {

    override val authorizer = object : BusinessLogicAuthorizer<BaseBo> {
        override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
            return
        }
    }

    override fun execute(executor: Executor, bo: Action): StringValue? =
        bo.returnValue?.let { StringValue(it) }

}