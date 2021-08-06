/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.datetime.Clock
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.module.module

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

    open val dispatcher by module<Dispatcher>()

    override val authorizer = SimpleRoleAuthorizer<Job> {
        all = adminRole
        update = createRole

        action(JobSuccess::class, workerRole)
        action(JobFail::class, workerRole)
        action(JobProgress::class, workerRole)
        action(JobCancel::class, workerRole)

        action(RequestJobCancel::class, adminRole)
    }

    override val router = router {
        action(JobSuccess::class, ::jobSuccess)
        action(JobFail::class, ::jobFail)
        action(JobProgress::class, ::jobProgress)
        action(JobCancel::class, ::jobCancel)

        action(RequestJobCancel::class, ::requestJobCancel)
    }

    val Job.completed
        get() = status == JobStatus.Succeeded || status == JobStatus.Failed || status == JobStatus.Cancelled


    override fun create(executor: Executor, bo: Job): Job {
        return super.create(executor, bo)
            .also {
                dispatcher.events.trySendBlocking(
                    JobCreateEvent(
                        jobId = it.id,
                        startAt = it.startAt,
                        specific = it.specific,
                        actionNamespace = it.actionNamespace,
                        actionType = it.actionType,
                        actionData = it.actionData
                    )
                ) // create the job even if sending to the dispatcher fails
            }
    }

    override fun delete(executor: Executor, entityId: EntityId<Job>) {
        val job = pa.readOrNull(entityId) ?: return

        check(job.completed) { "only completed jobs can be deleted, cancel the job first" }

        pa.delete(entityId)

        // no need to notify the dispatcher, succeeded or failed jobs are not in it
    }

    open fun jobSuccess(executor: Executor, action: JobSuccess): ActionStatusBo {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        job.status = JobStatus.Succeeded
        job.progress = 100.0
        job.responseData = action.responseData

        pa.update(job)

        dispatcher.events.trySendBlocking(
            JobSuccessEvent(
                actionNamespace = job.actionNamespace,
                actionType = job.actionType,
                jobId = job.id,
                specific = job.specific
            )
        ) // ignore errors, can't do much at this point

        return ActionStatusBo()
    }

    open fun jobProgress(executor: Executor, action: JobProgress): ActionStatusBo {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        job.status = JobStatus.Running
        job.progress = action.progress
        job.progressText = action.progressText
        job.lastProgressAt = Clock.System.now()

        pa.update(job)

        return ActionStatusBo()
    }

    open fun jobFail(executor: Executor, action: JobFail): ActionStatusBo {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        if (action.retryAt == null) {
            job.status = JobStatus.Failed
        } else {
            job.status = JobStatus.Pending
            job.startAt = action.retryAt
        }

        job.failCount ++
        job.progress = 0.0
        job.lastFailData = action.lastFailData

        pa.update(job)

        dispatcher.events.trySendBlocking(
            JobFailEvent(
                actionNamespace = job.actionNamespace,
                actionType = job.actionType,
                jobId = job.id,
                specific = job.specific,
                retryAt = action.retryAt,
                actionData = job.actionData
            )
        ) // ignore errors, can't do much at this point

        return ActionStatusBo()
    }

    open fun jobCancel(executor: Executor, action: JobCancel): ActionStatusBo {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        job.status = JobStatus.Cancelled

        pa.update(job)

        dispatcher.events.trySendBlocking(
            JobCancelEvent(
                actionNamespace = job.actionNamespace,
                actionType = job.actionType,
                jobId = job.id,
                specific = job.specific
            )
        ) // ignore errors, can't do much at this point

        return ActionStatusBo()
    }

    open fun requestJobCancel(executor: Executor, action: RequestJobCancel): ActionStatusBo {
        val job = pa.read(action.jobId)

        check(! job.completed) { "job has been already completed" }

        dispatcher.events.trySendBlocking(
            RequestJobCancelEvent(
                actionNamespace = job.actionNamespace,
                actionType = job.actionType,
                jobId = job.id,
                specific = job.specific
            )
        ) // ignore errors, can't do much at this point

        return ActionStatusBo()
    }
}