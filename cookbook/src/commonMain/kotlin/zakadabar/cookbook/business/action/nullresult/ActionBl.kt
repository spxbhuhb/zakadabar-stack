/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.action.nullresult

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.IntValue
import zakadabar.stack.util.PublicApi

@PublicApi
class ActionBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = Action.boNamespace

    override val router = router {
        action(Action::class, ::action)
    }

    override val authorizer by provider()

    fun action(executor: Executor, bo: Action): IntValue? =
        bo.returnValue?.let { IntValue(it.toInt()) }

}