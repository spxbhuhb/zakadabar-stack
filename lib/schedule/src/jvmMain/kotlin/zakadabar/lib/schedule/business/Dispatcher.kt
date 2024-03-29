/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.EntityId
import zakadabar.core.module.module
import zakadabar.core.util.UUID
import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.data.PushJob
import zakadabar.lib.schedule.data.Subscription
import kotlin.math.absoluteValue

class Dispatcher(
    val jobBl: JobBl
) {
    val events = Channel<DispatcherEvent>(UNLIMITED)

    val localScope = CoroutineScope(Dispatchers.IO)

    var coroutine = localScope.launch { run() }

    val subsciptionBl by module<SubscriptionBl>()

    @Serializable
    class JobEntry(
        val id: EntityId<Job>,
        val createdBy: UUID,
        val startAt: Instant?,
        val actionNamespace: String,
        val actionType: String,
        val actionData: String
    )

    @Serializable
    class SubscriptionEntry(
        val id: EntityId<Subscription>,
        val nodeId: UUID,
        val nodeComm: CommConfig,
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
            try {
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
            } catch (ex: Exception) {
                jobBl.logger.error("dispatcher error (event=$event)", ex)
            }
        }
    }

    /**
     * Add a job to [pendingJobs] or [runnableJobs], depending on
     * [Job.startAt]
     */
    fun onJobCreate(event: JobCreateEvent) {
        addJobEntry(event, event.createdBy, event.actionData, event.startAt) // calls pushJobs
    }

    fun addJobEntry(event: JobEvent, createdBy: UUID, actionData: String, startAt: Instant?) {
        val entry = JobEntry(
            event.jobId,
            createdBy,
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
        }.let { (it + 1).absoluteValue }

        pendingJobs.add(index, entry)

        jobBl.addPending(this)

        pushJobs()
    }

    fun takeRunEntry(jobId: EntityId<Job>, reuse: Boolean = true): RunEntry? {
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
        val runEntry = takeRunEntry(event.jobId)
        if (event.retryAt != null && runEntry != null) {
            addJobEntry(event, runEntry.job.createdBy, event.actionData, event.retryAt) // calls pushJobs
        }
        pushJobs()
    }

    fun onRequestJobCancel(event: RequestJobCancelEvent) {
        val id = event.jobId

        pendingJobs.indexOfFirst { it.id == id }.takeIf { it != - 1 }?.also {
            pendingJobs.removeAt(it)
            localScope.launch { jobBl.jobCancel(id) }
            return
        }

        runnableJobs.indexOfFirst { it.id == id }.takeIf { it != - 1 }?.also {
            runnableJobs.removeAt(it)
            localScope.launch { jobBl.jobCancel(id) }
            return
        }

        // TODO send cancel request to the worker
    }

    fun onSubscriptionCreate(event: SubscriptionCreateEvent) {
        idleSubscriptions += SubscriptionEntry(
            id = event.subscriptionId,
            nodeId = event.nodeId,
            nodeComm = event.nodeComm,
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
            localScope.launch { pushJob(entry) }
        }
    }

    suspend fun pushJob(entry: RunEntry) {
        val job = entry.job
        val subscription = entry.subscription

        jobBl.assignNode(job.id, subscription.nodeId)

        val executor = jobBl.accountBl.executorFor(job.createdBy)

        try {
            PushJob(
                jobId = job.id,
                actionNamespace = job.actionNamespace,
                actionType = job.actionType,
                actionData = job.actionData
            ).execute(executor, subscription.nodeComm)

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
        // FIXME what if the error is because of the job
        val entry = takeRunEntry(event.jobId, false) ?: return

        // do not reuse this subscription, delete from DB
        transaction {
            subsciptionBl.pa.delete(entry.subscription.id)
        }

        runnableJobs.add(0, entry.job)
        pushJobs()
    }

}