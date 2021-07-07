/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.businesslogic

import kotlinx.datetime.Clock
import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.data.Subscription
import zakadabar.stack.data.entity.EntityId
import kotlin.math.absoluteValue

open class Partition(
    val actionNamespace: String?,
    val actionType: String?
) {
    protected val pendingJobs = mutableListOf<Job>()
    protected val runnableJobs = mutableListOf<Job>()
    protected val runningJobs = mutableListOf<EntityId<Job>>()

    protected val availableSubscriptions = mutableListOf<Subscription>()
    protected val busySubscriptions = mutableListOf<Subscription>()

    /**
     * Add a job to [pendingJobs] or [runnableJobs], depending on
     * [Job.startAt]
     */
    fun add(job: Job) {
        if (job.runnable) {
            runnableJobs.add(job)
            return
        }

        val index = pendingJobs.binarySearch {
            val es = it.startAt
            val js = job.startAt
            if (es == null) {
                return@binarySearch if (js == null) 0 else - 1
            }
            if (js == null) {
                return@binarySearch 1
            }
            es.compareTo(js)
        }.absoluteValue

        pendingJobs.add(index, job)
    }

    /**
     * Remove a job from the jobs this partition handles.
     *
     * @param  jobId  Id of the job to remove.
     *
     * @throws  IllegalStateException  When the job is currently running.
     */
    fun removeJob(jobId: EntityId<Job>) {
        if (pendingJobs.removeAll { it.id == jobId }) return
        if (jobId in runningJobs) throw IllegalStateException("job is running")
    }

    fun add(subscription: Subscription) {
        availableSubscriptions += subscription
    }

    /**
     * Remove a subscription from the subscriptions this partition handles.
     *
     * @param  subscriptionId  Id of the subscription to remove.
     *
     * @throws  IllegalStateException  When the subscription is currently busy.
     */
    fun removeSubscription(subscriptionId : EntityId<Subscription>) {
        if (availableSubscriptions.removeAll { it.id == subscriptionId }) return
        if (busySubscriptions.firstOrNull { it.id == subscriptionId } != null) throw IllegalStateException("subscription is busy")
    }

    /**
     * True when the [Job.startAt] is null or is before now.
     */
    val Job.runnable : Boolean
        get() = startAt?.let { it <= Clock.System.now() } != false
}