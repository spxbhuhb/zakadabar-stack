/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.query.QueryBo
import kotlin.reflect.KClass

/**
 * Convenience base class for standalone query (without entity) business logics.
 */
abstract class QueryBusinessLogicCommon<RQ : QueryBo<RS>, RS : Any?>(
    protected val queryBoClass: KClass<RQ>
) : BusinessLogicCommon<BaseBo>(), QueryBusinessLogicWrapper {

    override val router = router {
        query(queryBoClass, ::execute)
    }

    abstract fun execute(executor: Executor, bo: RQ): RS

    override fun queryWrapper(executor: Executor, func: (Executor, BaseBo) -> Any?, bo: BaseBo): Any? {

        check(queryBoClass.isInstance(bo))

        @Suppress("UNCHECKED_CAST") // check above
        bo as RQ

        validator.validateQuery(executor, bo)

        authorizer.authorizeQuery(executor, bo)

        val response = execute(executor, bo)

        auditor.auditQuery(executor, bo)

        return response
    }

}