/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.ActionBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo
import zakadabar.core.data.StringValue

class TestBl : EntityBusinessLogicBase<TestBo>(
    boClass = TestBo::class
) {

    override val pa = TestPa()

    override val authorizer = object : BusinessLogicAuthorizer<TestBo> {

        override fun authorizeList(executor: Executor) {
            return
        }

        override fun authorizeRead(executor: Executor, entityId: EntityId<TestBo>) {
            return
        }

        override fun authorizeCreate(executor: Executor, entity: TestBo) {
            return
        }

        override fun authorizeUpdate(executor: Executor, entity: TestBo) {
            return
        }

        override fun authorizeDelete(executor: Executor, entityId: EntityId<TestBo>) {
            return
        }

        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            return
        }

        override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
            return
        }
    }

    override val router = router {
        query(TestQuery::class, ::testQuery)
        query(TestQueryNull::class, ::testQueryNull)
        action(TestAction::class, ::testAction)
        action(TestActionNull::class, ::testActionNull)
    }

    fun testQuery(executor: Executor, bo : TestQuery) : StringValue {
        return StringValue(bo.returnValue)
    }

    fun testQueryNull(executor: Executor, bo : TestQueryNull) : StringValue? {
        return bo.returnValue?.let { StringValue(it) }
    }

    fun testAction(executor: Executor, bo : TestAction) : StringValue {
        return StringValue("test action result")
    }

    fun testActionNull(executor: Executor, bo : TestActionNull) : StringValue? {
        return bo.returnValue?.let { StringValue(it) }
    }
}