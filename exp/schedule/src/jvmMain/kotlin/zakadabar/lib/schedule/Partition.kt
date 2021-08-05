/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Clock
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Lock
import zakadabar.stack.util.use
import kotlin.math.absoluteValue

/**
 * Stores jobs and subscriptions for the given namespace, type pair.
 *
 * @property  pendingJobs   Jobs that has a start time and it is in the future.
 * @property  runnableJobs  Jobs that can be started right now.
 * @property  runningJobs   Jobs that are currently running.
 */
open class Partition(
    val actionNamespace: String?,
    val actionType: String?
) {
    protected val lock = Lock()

    protected var pendingJobs = mutableListOf<PartitionJobEntry>()
    protected val runnableJobs = mutableListOf<PartitionJobEntry>()
    protected val runningJobs = mutableListOf<PartitionJobEntry>()

    protected val idleSubscriptions = mutableListOf<Subscription>()
    protected val busySubscriptions = mutableListOf<Subscription>()

    /**
     * Add a job to [pendingJobs] or [runnableJobs], depending on
     * [Job.startAt]
     */
    fun add(entry: PartitionJobEntry) {

        // I opted to for two 'lock.use' instead of one so getting the time
        // from the clock is not under the lock.

        if (entry.runnable) {
            lock.use { runnableJobs.add(entry) }
            return
        }

        lock.use {
            val index = pendingJobs.binarySearch {
                val es = it.startAt
                val js = entry.startAt
                if (es == null) {
                    return@binarySearch if (js == null) 0 else - 1
                }
                if (js == null) {
                    return@binarySearch 1
                }
                es.compareTo(js)
            }.absoluteValue

            pendingJobs.add(index, entry)
        }
    }

    /**
     * Remove a job from the jobs this partition handles.
     *
     * @param  jobId  Id of the job to remove.
     *
     * @throws  IllegalStateException  When the job is currently running.
     */
    fun removeJob(jobId: EntityId<Job>) {
        lock.use {
            if (pendingJobs.removeAll { it.jobId == jobId }) return
            if (runnableJobs.removeAll { it.jobId == jobId }) return
            if (runningJobs.find { it.jobId == jobId } != null) throw IllegalStateException("job is running")
        }
    }

    fun add(subscription: Subscription) {
        lock.use {
            idleSubscriptions += subscription
        }
    }

    /**
     * Remove a subscription from the subscriptions this partition handles.
     *
     * @param  subscriptionId  Id of the subscription to remove.
     *
     * @throws  IllegalStateException  When the subscription is currently busy.
     */
    fun removeSubscription(subscriptionId: EntityId<Subscription>) {
        lock.use {
            if (idleSubscriptions.removeAll { it.id == subscriptionId }) return
            if (busySubscriptions.firstOrNull { it.id == subscriptionId } != null) throw IllegalStateException("subscription is busy")
        }
    }

    /**
     * True when the [Job.startAt] is null or is before now.
     */
    val PartitionJobEntry.runnable: Boolean
        get() = startAt?.let { it <= Clock.System.now() } != false

    /**
     * Move jobs from [pendingJobs] to [runnableJobs] when [Job.startAt] in the past.
     */
    fun pendingToRunnable(events: Channel<DispatcherEvent>) {
        lock.use {
            if (pendingJobs.isEmpty()) return

            val now = Clock.System.now()

            val jobs = pendingJobs.takeWhile { entry ->
                entry.startAt?.let { it <= now } ?: true
            }

            if (jobs.isEmpty()) return

            pendingJobs = pendingJobs.drop(jobs.size).toMutableList()
            runnableJobs.addAll(jobs)
        }
    }
}