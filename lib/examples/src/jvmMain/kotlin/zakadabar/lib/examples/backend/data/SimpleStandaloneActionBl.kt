/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import zakadabar.lib.examples.data.SimpleStandaloneAction
import zakadabar.stack.backend.business.ActionBusinessLogicBase
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.util.Executor

abstract class SimpleStandaloneActionBl : ActionBusinessLogicBase<SimpleStandaloneAction, ActionStatusBo>(
    actionBoClass = SimpleStandaloneAction::class
) {

    override fun execute(executor: Executor, bo: SimpleStandaloneAction): ActionStatusBo {
        return ActionStatusBo(reason = "test action")
    }

}