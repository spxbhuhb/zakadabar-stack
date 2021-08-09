/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.multi

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.LongValue

open class MultiBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = "zkc-multi-bl"

    override val authorizer by provider()

    override val router = router {
        action(Action1::class, ::action1)
        action(Action2::class, ::action2)
        query(Query1::class, ::query1)
        query(Query2::class, ::query2)
    }

    private fun action1(executor: Executor, action1: Action1): LongValue {
        TODO("Not yet implemented")
    }

    private fun action2(executor: Executor, action2: Action2): LongValue {
        TODO("Not yet implemented")
    }

    private fun query1(executor: Executor, query1: Query1): List<String> {
        TODO("Not yet implemented")
    }

    private fun query2(executor: Executor, query2: Query2): List<String> {
        TODO("Not yet implemented")
    }


}