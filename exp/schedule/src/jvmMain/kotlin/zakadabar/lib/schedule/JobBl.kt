/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.coroutines.channels.trySendBlocking
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.module.module

/**
 * Business Logic for Job.
 *
 * @param   adminRole        Role for: list, read, update, delete.
 * @param   createRole       Role for: create
 * @param   dispatcherRole   Role for: Poll, UpdateStatus
 */
open class JobBl(
    protected val adminRole: String,
    protected val createRole: String,
    protected val dispatcherRole: String
) : EntityBusinessLogicBase<Job>(
    boClass = Job::class
) {

    override val pa = JobPa()

    open val dispatcher by module<Dispatcher>()

    override val authorizer = SimpleRoleAuthorizer<Job> {
        all = adminRole
        update = createRole
        action(StatusUpdate::class, dispatcherRole)
    }

    override val router = router {
        action(StatusUpdate::class, ::updateStatus)
    }

    override fun create(executor: Executor, bo: Job): Job {
        return super.create(executor, bo)
            .also {
                dispatcher.events.trySendBlocking(
                    JobCreateEvent(
                        jobId = it.id,
                        startAt = it.startAt,
                        specific = it.specific,
                        actionNamespace = it.actionNamespace,
                        actionType = it.actionType
                    )
                ) // create the job even if sending to the dispatcher fails
            }
    }

    open fun updateStatus(executor: Executor, action: StatusUpdate): ActionStatusBo {
        val job = pa.read(action.jobId)

        job.status = action.status
        job.progress = action.progress
        job.responseData = job.responseData

        pa.update(job)

        dispatcher.events.trySendBlocking(
            JobStatusUpdateEvent(
                jobId = job.id,
                status = job.status
            )
        ) // ignore errors, can't do much at this point

        return ActionStatusBo()
    }

}