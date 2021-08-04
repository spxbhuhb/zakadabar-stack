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
            it.value.pendingToRunnable()
        }
    }

    open suspend fun dispatch() {
        for (event in events) {
            when (event) {
                is JobCreateEvent -> onJobCreate(event)
                is JobStatusUpdateEvent -> onJobStatusUpdate(event)
                is SubscriptionCreateEvent -> onSubscriptionCreate(event)
                is SubscriptionDeleteEvent -> onSubscriptionDelete(event)
            }
        }
    }

    protected fun onJobCreate(event: JobCreateEvent) {
        TODO("Not yet implemented")
    }

    protected fun onJobStatusUpdate(event: JobStatusUpdateEvent) {
        TODO("Not yet implemented")
    }

    private fun onSubscriptionCreate(event: SubscriptionCreateEvent) {
        TODO("Not yet implemented")
    }

    private fun onSubscriptionDelete(event: SubscriptionDeleteEvent) {
        TODO("Not yet implemented")
    }
}