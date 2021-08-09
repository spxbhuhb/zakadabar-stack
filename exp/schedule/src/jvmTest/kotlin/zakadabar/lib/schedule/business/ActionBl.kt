/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.IntValue
import zakadabar.stack.util.Lock
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.use

@PublicApi
class ActionBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = Action.boNamespace

    override val authorizer by provider()

    override val router = router {
        action(Action::class, ::action)
    }

    val lock = Lock()

    var channel = Channel<Long?>()
        get() = lock.use { field }
        set(value) {
            lock.use{ field = value }
        }

    fun action(executor: Executor, bo: Action): IntValue? {
        channel.trySendBlocking(bo.returnValue)
        return bo.returnValue?.let { IntValue(it.toInt()) }
    }

}