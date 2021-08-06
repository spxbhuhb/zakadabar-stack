/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.UUID
import kotlin.math.absoluteValue

class Partition(
    val key: PartitionKey
) {
    val events = Channel<DispatcherEvent>()

    var coroutine = runBlocking { launch { run() } }

    class JobEntry(
        val id: EntityId<Job>,
        val startAt: Instant?,
        val actionNamespace: String,
        val actionType: String,
        val actionData: String
    )

    class SubscriptionEntry(
        val id: EntityId<Subscription>,
        val nodeUrl: String,
        val nodeId: UUID,
        var reuse: Boolean
    )

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
            when (event) {
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
        addJobEntry(event, event.actionData, event.startAt)
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
            return
        }

        val index = pendingJobs.binarySearch {
            it.startAt !!.compareTo(startAt)
        }.absoluteValue

        pendingJobs.add(index, entry)
    }

    fun takeRunEntry(jobId: EntityId<Job>): RunEntry? {
        val index = runningJobs.indexOfFirst { it.job.id == jobId }
        if (index == - 1) return null

        val entry = runningJobs.removeAt(index)
        if (entry.subscription.reuse) idleSubscriptions += entry.subscription

        return entry
    }

    fun onJobSuccess(event: JobSuccessEvent) {
        takeRunEntry(event.jobId)
    }

    fun onJobFail(event: JobFailEvent) {
        takeRunEntry(event.jobId) ?: return
        if (event.retryAt != null) {
            addJobEntry(event, event.actionData, event.retryAt)
        }
    }

    fun onRequestJobCancel(event: RequestJobCancelEvent) {
        val id = event.jobId

        pendingJobs.indexOfFirst { it.id == id }.takeIf { it != - 1 }?.also {
            val entry = pendingJobs.removeAt(it)
            runBlocking { launch { JobCancel(entry.id).execute() } }
            return
        }

        runnableJobs.indexOfFirst { it.id == id }.takeIf { it != - 1 }?.also {
            val entry = runnableJobs.removeAt(it)
            runBlocking { launch { JobCancel(entry.id).execute() } }
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
    fun pendingToRunnable(events: Channel<DispatcherEvent>) {
        if (pendingJobs.isEmpty()) return

        val now = Clock.System.now()

        val jobs = pendingJobs.takeWhile { entry ->
            entry.startAt?.let { it <= now } ?: true
        }

        if (jobs.isEmpty()) return

        pendingJobs = pendingJobs.drop(jobs.size).toMutableList()
        runnableJobs.addAll(jobs)
    }


    fun pushJobs() {
        runBlocking {
            while (idleSubscriptions.isNotEmpty() && runnableJobs.isNotEmpty()) {
                val entry = RunEntry(runnableJobs.first(), idleSubscriptions.first())
                runningJobs += entry

                launch { pushJob(entry) }
            }
        }
    }

    suspend fun pushJob(entry: RunEntry) {
        val job = entry.job
        val subscription = entry.subscription

        try {
            PushJob(
                jobId = job.id,
                actionNamespace = job.actionNamespace,
                actionType = job.actionType,
                actionData = job.actionData
            ).execute(baseUrl = subscription.nodeUrl)
        } catch (ex: Exception) {
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

    fun onPushFail(event : PushFailEvent) {
        val entry = takeRunEntry(event.jobId) ?: return
        runnableJobs.add(0, entry.job)
        pushJobs()
    }

}