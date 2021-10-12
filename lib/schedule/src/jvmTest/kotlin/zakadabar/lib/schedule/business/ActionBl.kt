/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import zakadabar.core.authorize.Executor
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.data.IntValue
import zakadabar.core.util.Lock
import zakadabar.core.util.PublicApi
import zakadabar.core.util.use
import java.lang.Thread.sleep

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
        sleep(100)
        channel.trySendBlocking(bo.returnValue)
        return bo.returnValue?.let { IntValue(it.toInt()) }
    }

}