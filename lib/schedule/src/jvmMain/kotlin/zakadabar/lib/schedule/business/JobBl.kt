/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable
import zakadabar.core.authorize.AccountBlProvider
import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.EntityId
import zakadabar.core.module.module
import zakadabar.core.util.Lock
import zakadabar.core.util.UUID
import zakadabar.core.util.use
import zakadabar.lib.schedule.data.*
import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.peristence.JobPa
import kotlin.coroutines.coroutineContext

/**
 * Business Logic for Job.
 *
 * @param   adminRole        Role for: list, read, update, delete.
 * @param   createRole       Role for: create
 * @param   workerRole       Role for: JobSuccess, JobProgress, JobFail
 */
open class JobBl(
    protected val adminRole: String,
    protected val createRole: String,
    protected val workerRole: String
) : EntityBusinessLogicBase<Job>(
    boClass = Job::class
) {

    override val pa = JobPa()

    protected val dispatcherLock = Lock()

    protected val pendingCheckEvent = PendingCheckEvent()

    protected val localScope = CoroutineScope(Dispatchers.IO)

    @Serializable
    data class DispatcherKey( // data class, so hash and equals uses fields
        val actionNamespace: String?,
        val actionType: String?
    )

    protected val dispatchers = mutableMapOf<DispatcherKey, Dispatcher>()

    protected val pendingDispatchers = mutableListOf<Dispatcher>()

    protected lateinit var periodic: kotlinx.coroutines.Job

    val accountBl by module<AccountBlProvider>()

    override val authorizer = SimpleRoleAuthorizer<Job> {
        all = adminRole
        create = createRole
        update = createRole

        action(JobSuccess::class, workerRole)
        action(JobFail::class, workerRole)
        action(JobProgress::class, workerRole)
        action(JobCancel::class, workerRole)

        action(RequestJobCancel::class, adminRole)
        query(JobSummary::class, adminRole)
    }

    override val router = router {
        action(JobSuccess::class, ::jobSuccess)
        action(JobFail::class, ::jobFail)
        action(JobProgress::class, ::jobProgress)
        action(JobCancel::class, ::jobCancel)

        action(RequestJobCancel::class, ::requestJobCancel)
        query(JobSummary::class, ::jobSummary)
    }

    val Job.completed
        get() = status == JobStatus.Succeeded || status == JobStatus.Failed || status == JobStatus.Cancelled

    override fun onModuleStart() {
        super.onModuleStart()
        periodic = localScope.launch { run() }
    }

    override fun onInitializeDb() {
        super.onInitializeDb()
        pa.readPendingJobs().forEach {
            it.dispatch()
        }
    }

    override fun onModuleStop() {
        localScope.cancel()
        dispatchers.values.forEach {
            it.localScope.cancel()
        }
        super.onModuleStop()
    }

    open suspend fun run() {
        while (coroutineContext.isActive) {
            dispatcherLock.use {
                pendingDispatchers.forEach {
                    it.events.send(pendingCheckEvent)
                }
            }
            delay(500)
        }
    }

    fun removePending(dispatcher: Dispatcher) {
        dispatcherLock.use {
            pendingDispatchers.removeAll { it == dispatcher }
        }
    }

    fun addPending(dispatcher: Dispatcher) {
        dispatcherLock.use {
            pendingDispatchers
                .indexOf(dispatcher)
                .let {
                    if (it == - 1) pendingDispatchers.add(dispatcher)
                }
        }
    }

    override fun create(executor: Executor, bo: Job): Job {
        bo.createdBy = executor.accountUuid

        return super.create(executor, bo)
            .also {
                JobCreateEvent(
                    jobId = it.id,
                    startAt = it.startAt,
                    specific = it.specific,
                    createdBy = executor.accountUuid,
                    actionNamespace = it.actionNamespace,
                    actionType = it.actionType,
                    actionData = it.actionData
                ).dispatch()
            }
    }

    override fun delete(executor: Executor, entityId: EntityId<Job>) {
        val job = pa.readOrNull(entityId) ?: return

        check(job.completed) { "only completed jobs can be deleted, cancel the job first" }

        pa.delete(entityId)

        // no need to notify the dispatcher, succeeded or failed jobs are not in it
    }

    open fun jobSuccess(executor: Executor, action: JobSuccess) {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        job.status = JobStatus.Succeeded
        job.progress = 100.0
        job.responseData = action.responseData

        pa.update(job)

        JobSuccessEvent(
            actionNamespace = job.actionNamespace,
            actionType = job.actionType,
            jobId = job.id,
            specific = job.specific
        ).dispatch()

    }

    open fun jobProgress(executor: Executor, action: JobProgress) {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        job.status = JobStatus.Running
        job.progress = action.progress
        job.progressText = action.progressText
        job.lastProgressAt = Clock.System.now()

        pa.update(job)

    }

    open fun jobFail(executor: Executor, action: JobFail) {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        val now = Clock.System.now()

        job.failCount ++
        job.progress = 0.0
        job.lastFailedAt = now
        job.lastFailMessage = action.lastFailMessage
        job.lastFailData = action.lastFailData

        // retry = 0, fail = 1 -> no retry
        // retry = 1, fail = 1 -> retry
        // retry = 1, fail = 2 -> no retry
        // retry = 2, fail = 1 -> retry
        // retry = 2, fail = 2 -> retry
        // retry = 2, fail = 3 -> no retry

        var retryAt : Instant? = null

        if (job.retryCount < job.failCount) {
            // no retry
            job.status = JobStatus.Failed
        } else {
            // retry
            job.status = JobStatus.Pending
            retryAt = action.retryAt ?: now.plus(job.retryInterval, DateTimeUnit.SECOND)
            job.startAt = retryAt // so, system restart won't start the job immediately
        }

        pa.update(job)

        JobFailEvent(
            actionNamespace = job.actionNamespace,
            actionType = job.actionType,
            jobId = job.id,
            specific = job.specific,
            retryAt = retryAt,
            actionData = job.actionData
        ).dispatch()

    }

    open fun jobCancel(executor: Executor, action: JobCancel) =
        jobCancel(action.jobId)

    open fun jobCancel(id: EntityId<Job>) {

        pa.withTransaction { // jobCancel is called from Dispatchers, so we need a transaction

            val job = pa.read(id)

            check(! job.completed) { "job has been already completed" }

            job.status = JobStatus.Cancelled

            pa.update(job)

            JobCancelEvent(
                actionNamespace = job.actionNamespace,
                actionType = job.actionType,
                jobId = job.id,
                specific = job.specific
            ).dispatch()
        }

    }

    open fun requestJobCancel(executor: Executor, action: RequestJobCancel) {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        RequestJobCancelEvent(
            actionNamespace = job.actionNamespace,
            actionType = job.actionType,
            jobId = job.id,
            specific = job.specific
        ).dispatch()

    }

    open fun jobSummary(executor: Executor, query : JobSummary) : List<JobSummaryEntry> =
        pa.jobSummary()

    open fun assignNode(jobId: EntityId<Job>, node: UUID) {
        pa.withTransaction {
            pa.assignNode(jobId, node)
        }
    }

    open fun dispatchEvent(event: DispatcherEvent) {
        event.dispatch()
    }

    open fun DispatcherEvent.dispatch() {

        // Jobs are specific only when marked as specific explicitly.
        // Subscriptions are specific when namespace or type is specified.

        val specific = if (this is JobEvent) {
            specific
        } else {
            actionNamespace != null || actionType != null
        }

        // All non-specific events go to the default dispatcher

        val key = if (specific) {
            DispatcherKey(actionNamespace, actionType)
        } else {
            DispatcherKey(null, null)
        }

        dispatcherLock.use {
            dispatchers
                .getOrPut(key) { Dispatcher(this@JobBl) }
                .events
        }.trySendBlocking(this)

    }
}