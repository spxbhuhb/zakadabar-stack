/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import kotlin.reflect.KClass

/**
 * Convenience base class for standalone action (without entity) business logics.
 */
abstract class ActionBusinessLogicCommon<RQ : ActionBo<RS>, RS : Any>(
    open val actionBoClass: KClass<RQ>
) : BusinessLogicCommon<BaseBo>(), ActionBusinessLogicWrapper {

    override val router = router {
        action(actionBoClass, ::execute)
    }

    abstract fun execute(executor: Executor, bo: RQ): RS

    override fun actionWrapper(executor: Executor, func: (Executor, BaseBo) -> Any, bo: BaseBo): Any {

        check(actionBoClass.isInstance(bo))

        @Suppress("UNCHECKED_CAST") // check above
        bo as RQ

        validator.validateAction(executor, bo)

        authorizer.authorizeAction(executor, bo)

        val response = execute(executor, bo)

        auditor.auditAction(executor, bo)

        return response

    }
}