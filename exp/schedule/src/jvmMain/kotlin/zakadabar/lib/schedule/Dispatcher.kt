/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import zakadabar.stack.module.CommonModule

open class Dispatcher : CommonModule {

    open val events = Channel<DispatcherEvent>(UNLIMITED)

    protected val partitions = mutableMapOf<PartitionKey, Partition>()

    lateinit var timedStart: kotlinx.coroutines.Job
    lateinit var dispatcher: kotlinx.coroutines.Job

    @DelicateCoroutinesApi
    override fun onModuleStart() {
        super.onModuleStart()
        timedStart = GlobalScope.launch {
            timedStart()
        }
        dispatcher = GlobalScope.launch {
            dispatch()
        }
    }

    override fun onModuleStop() {
        super.onModuleStop()
        timedStart.cancel()
        events.close()
    }

    open suspend fun timedStart() {
        partitions.forEach {

        }
    }

    open suspend fun dispatch() {
        for (event in events) {

            // Jobs are specific only when marked as specific explicitly.
            // Subscriptions are specific when namespace or type is specified.

            val specific = if (event is JobEvent) {
                event.specific
            } else {
                event.actionNamespace != null || event.actionType != null
            }

            // All non-specific events go to the default partition

            val key = if (specific) {
                PartitionKey(event.actionNamespace, event.actionType)
            } else {
                PartitionKey(null, null)
            }

            partitions
                .getOrPut(key) { Partition(key) }
                .events.send(event)
        }
    }

}