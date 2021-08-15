/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import zakadabar.lib.examples.data.SimpleStandaloneAction
import zakadabar.core.authorize.Executor
import zakadabar.core.business.ActionBusinessLogicBase
import zakadabar.core.data.ActionStatus

abstract class SimpleStandaloneActionBl : ActionBusinessLogicBase<SimpleStandaloneAction, ActionStatus>(
    actionBoClass = SimpleStandaloneAction::class
) {

    override fun execute(executor: Executor, bo: SimpleStandaloneAction): ActionStatus {
        return ActionStatus(reason = "test action")
    }

}