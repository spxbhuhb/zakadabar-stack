/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.businesslogic

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.data.Subscription
import zakadabar.lib.schedule.persistenceapi.SubscriptionExposedPaGen
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.data.entity.EntityId

open class SubscriptionBl : EntityBusinessLogicBase<Subscription>(
    boClass = Subscription::class
) {

    override val pa = SubscriptionExposedPaGen()

    override val authorizer by provider()

    lateinit var timedStart: kotlinx.coroutines.Job
    lateinit var dispatcher: kotlinx.coroutines.Job

    /**
     * [Job]s and [Subscription]s partitioned by [Job.actionNamespace]
     * and [Job.actionType] pairs.
     */
    protected val partitions = mapOf<PartitionKey,Partition>()

    open val dispatcherNotify = Channel<Unit>(UNLIMITED)

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
        dispatcherNotify.close()
    }

    /**
     * Checks [pendingJobs] and [availableSubscriptions] every 500 milliseconds
     * and sends a Unit object into [dispatcherNotify] when both are non-empty
     * and the start time of the first pending job is passed.
     */
    open suspend fun timedStart() {
//        while (coroutineContext.isActive) {
//            val canDispatch = synchronized(lock) {
//                pendingJobs.isNotEmpty() &&
//                        pendingJobs.first().startAt?.let { it <= Clock.System.now() } != false &&
//                        availableSubscriptions.isNotEmpty()
//            }
//            if (canDispatch) {
//                dispatcherNotify.send(Unit)
//            }
//            delay(500)
//        }
    }

    open suspend fun dispatch() {
//        for (event in dispatcherNotify) {
//            var again: Boolean = true
//            while (again) {
//                again = false
//                synchronized(lock) {
//                    if (pendingJobs.isEmpty()) return@synchronized
//                    if (availableSubscriptions.isEmpty()) return@synchronized
//
//                    val job = pendingJobs.first()
//                    if (job.startAt?.let { it > Clock.System.now() } == true) return@synchronized
//
//                    availableSubscriptions.firstOrNull {
//                        (it.actionNamespace == null && it.actionType == null) ||
//                                (it.actionNamespace == job.actionNamespace || it.actionType == job.actionType)
//                    }
//
//                    again = true
//                }
//            }
//        }
    }

    override fun create(executor: Executor, bo: Subscription): Subscription {
        val created = super.create(executor, bo)

        return created
    }

    override fun update(executor: Executor, bo: Subscription): Subscription {
        throw NotImplementedError("subscriptions cannot be updated, delete and create a new")
    }

    override fun delete(executor: Executor, entityId: EntityId<Subscription>) {
        super.delete(executor, entityId)

//        synchronized(lock) {
//        }

    }

    suspend fun removeJob(jobId: EntityId<Job>) {

    }

}