/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import zakadabar.lib.examples.data.SimpleExampleAction
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.lib.examples.data.SimpleExampleQuery
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.data.entity.EntityBusinessLogicBase
import zakadabar.stack.backend.validate.Validator
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.util.Executor

class SimpleExampleBl : EntityBusinessLogicBase<SimpleExampleBo>(
    boClass = SimpleExampleBo::class
) {

    override val pa = SimpleExampleExposedPa()

    override val authorizer = SimpleRoleAuthorizer<SimpleExampleBo> {
        all = StackRoles.siteMember
        action(SimpleExampleAction::class, StackRoles.siteMember)
        query(SimpleExampleQuery::class, StackRoles.siteMember)
    }

    override val router = router {
        action(SimpleExampleAction::class, ::action)
    }

    override val validator = object : Validator<SimpleExampleBo> {
        override fun validateCreate(executor: Executor, bo: SimpleExampleBo) {
            println("Incoming BO is ${if (bo.isValid) "valid" else "invalid"}.")
        }
    }

    override val auditor = auditor {
        includeData = false
    }

    override fun update(executor: Executor, bo: SimpleExampleBo) =
        pa.read(bo.id)
            .let {
                it.name = bo.name.lowercase()
                pa.update(it)
            }

    private fun action(executor: Executor, action: SimpleExampleAction): ActionStatusBo {
        println("Account ${executor.accountId} executed SimpleExampleAction")
        return ActionStatusBo()
    }

    private fun query(executor: Executor, query: SimpleExampleQuery) =
        pa.query(query)

}