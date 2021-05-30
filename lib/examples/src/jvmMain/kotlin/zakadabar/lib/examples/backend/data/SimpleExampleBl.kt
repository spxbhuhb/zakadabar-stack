/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import zakadabar.lib.examples.data.SimpleExampleAction
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.lib.examples.data.SimpleExampleQuery
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.UnsafeAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.server
import zakadabar.stack.backend.validate.Validator
import zakadabar.stack.data.builtin.ActionStatusBo

class SimpleExampleBl : EntityBusinessLogicBase<SimpleExampleBo>(
    boClass = SimpleExampleBo::class
) {

    override val pa = SimpleExampleExposedPa()

    override val authorizer = UnsafeAuthorizer<SimpleExampleBo>()

    override val router = router {
        action(SimpleExampleAction::class, ::action)
        query(SimpleExampleQuery::class, ::query)
    }

    override val validator = object : Validator<SimpleExampleBo> {
        override fun validateCreate(executor: Executor, bo: SimpleExampleBo) {
            auditor.auditCustom(executor) { "Incoming BO is ${if (bo.isValid) "valid" else "invalid"}." }
        }
    }

    override val auditor = auditor {
        includeData = false
    }

    private val simpleExampleBl by lazy { server.first<SimpleExampleBl>() }

    override fun create(executor: Executor, bo: SimpleExampleBo) : SimpleExampleBo {
        if (pa.count() >= 1000) throw IllegalStateException("table limit reached")

        return pa.create(bo)
            .let {
                it.name = bo.name.lowercase()
                pa.update(it)
            }
    }

    override fun update(executor: Executor, bo: SimpleExampleBo) =
        pa.read(bo.id)
            .let {
                it.name = bo.name.lowercase()
                pa.update(it)
            }

    private fun action(executor: Executor, action: SimpleExampleAction): ActionStatusBo {
        println("Account ${executor.accountId} executed SimpleExampleAction")
        return ActionStatusBo(reason = "This is a successful test action invocation.")
    }

    private fun query(executor: Executor, query: SimpleExampleQuery) =
        pa.query(query)

}