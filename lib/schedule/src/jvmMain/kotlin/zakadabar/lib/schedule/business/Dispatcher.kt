/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityId
import zakadabar.core.util.UUID
import zakadabar.core.util.fork
import zakadabar.lib.schedule.api.Job
import zakadabar.lib.schedule.api.PushJob
import zakadabar.lib.schedule.api.Subscription
import kotlin.math.absoluteValue

class Dispatcher(
    val jobBl: JobBl
) {
    val events = Channel<DispatcherEvent>(UNLIMITED)

    var coroutine = fork { run() }

    @Serializable
    class JobEntry(
        val id: EntityId<Job>,
        val startAt: Instant?,
        val actionNamespace: String,
        val actionType: String,
        val actionData: String
    )

    @Serializable
    class SubscriptionEntry(
        val id: EntityId<Subscription>,
        val nodeUrl: String,
        val nodeId: UUID,
        var reuse: Boolean
    )

    @Serializable
    class RunEntry(
        val job: JobEntry,
        val subscription: SubscriptionEntry
    )

    var pendingJobs = mutableListOf<JobEntry>()
    val runnableJobs = mutableListOf<JobEntry>()
    val runningJobs = mutableListOf<RunEntry>()
    val idleSubscriptions = mutableListOf<SubscriptionEntry>()

    suspend fun run() {
        for (event in events) {
            println("dispatcher event: $event")
            when (event) {
                is PendingCheckEvent -> onPendingCheck()
                is JobCreateEvent -> onJobCreate(event)
                is JobSuccessEvent -> onJobSuccess(event)
                is JobFailEvent -> onJobFail(event)
                is RequestJobCancelEvent -> onRequestJobCancel(event)
                is SubscriptionCreateEvent -> onSubscriptionCreate(event)
                is SubscriptionDeleteEvent -> onSubscriptionDelete(event)
                is PushFailEvent -> onPushFail(event)
            }
        }
    }

    /**
     * Add a job to [pendingJobs] or [runnableJobs], depending on
     * [Job.startAt]
     */
    fun onJobCreate(event: JobCreateEvent) {
        addJobEntry(event, event.actionData, event.startAt) // calls pushJobs
    }

    fun addJobEntry(event: JobEvent, actionData: String, startAt: Instant?) {
        val entry = JobEntry(
            event.jobId,
            startAt,
            event.actionNamespace,
            event.actionType,
            actionData
        )

        if (startAt == null || startAt <= Clock.System.now()) {
            runnableJobs.add(entry)
            pushJobs()
            return
        }

        val index = pendingJobs.binarySearch {
            it.startAt !!.compareTo(startAt)
        }.absoluteValue

        pendingJobs.add(index, entry)

        jobBl.addPending(this)

        pushJobs()
    }

    fun takeRunEntry(jobId: EntityId<Job>, reuse : Boolean = true): RunEntry? {
        val index = runningJobs.indexOfFirst { it.job.id == jobId }
        if (index == - 1) return null

        val entry = runningJobs.removeAt(index)
        if (reuse && entry.subscription.reuse) idleSubscriptions += entry.subscription

        return entry
    }

    fun onJobSuccess(event: JobSuccessEvent) {
        takeRunEntry(event.jobId)
        pushJobs()
    }

    fun onJobFail(event: JobFailEvent) {
        takeRunEntry(event.jobId)
        if (event.retryAt != null) {
            addJobEntry(event, event.actionData, event.retryAt) // calls pushJobs
        }
    }

    fun onRequestJobCancel(event: RequestJobCancelEvent) {
        val id = event.jobId

        pendingJobs.indexOfFirst { it.id == id }.takeIf { it != - 1 }?.also {
            pendingJobs.removeAt(it)
            fork { jobBl.jobCancel(id) }
            return
        }

        runnableJobs.indexOfFirst { it.id == id }.takeIf { it != - 1 }?.also {
            runnableJobs.removeAt(it)
            fork { jobBl.jobCancel(id) }
            return
        }

        // TODO send cancel request to the worker
    }

    fun onSubscriptionCreate(event: SubscriptionCreateEvent) {
        idleSubscriptions += SubscriptionEntry(
            id = event.subscriptionId,
            nodeUrl = event.nodeUrl,
            nodeId = event.nodeId,
            reuse = true
        )
        pushJobs()
    }

    fun onSubscriptionDelete(event: SubscriptionDeleteEvent) {
        if (idleSubscriptions.removeAll { it.id == event.subscriptionId }) return

        runningJobs
            .firstOrNull { it.subscription.id == event.subscriptionId }
            ?.subscription
            ?.reuse = false
    }

    /**
     * Move jobs from [pendingJobs] to [runnableJobs] when [Job.startAt] in the past.
     */
    fun onPendingCheck() {
        if (pendingJobs.isEmpty()) {
            jobBl.removePending(this)
            return
        }

        val now = Clock.System.now()

        val jobs = pendingJobs.takeWhile { entry ->
            entry.startAt?.let { it <= now } ?: true
        }

        if (jobs.isEmpty()) return

        pendingJobs = pendingJobs.drop(jobs.size).toMutableList()
        runnableJobs.addAll(jobs)

        pushJobs()
    }

    fun pushJobs() {
        while (idleSubscriptions.isNotEmpty() && runnableJobs.isNotEmpty()) {
            val entry = RunEntry(runnableJobs.removeAt(0), idleSubscriptions.removeAt(0))
            runningJobs += entry
            fork { pushJob(entry) }
        }
    }

    suspend fun pushJob(entry: RunEntry) {
        val job = entry.job
        val subscription = entry.subscription

        jobBl.assignNode(job.id, subscription.nodeId)

        try {
            PushJob(
                jobId = job.id,
                actionNamespace = job.actionNamespace,
                actionType = job.actionType,
                actionData = job.actionData
            ).execute(baseUrl = subscription.nodeUrl)
        } catch (ex: Exception) {
            jobBl.alarmSupport.create(ex)
            events.send(
                PushFailEvent(
                    jobId = job.id,
                    actionNamespace = job.actionNamespace,
                    actionType = job.actionType,
                    specific = false
                )
            )
            return
        }

    }

    fun onPushFail(event: PushFailEvent) {
        // do not reuse this subscription
        // FIXME what if the error is because of the job
        val entry = takeRunEntry(event.jobId, false) ?: return
        runnableJobs.add(0, entry.job)
        pushJobs()
    }

}