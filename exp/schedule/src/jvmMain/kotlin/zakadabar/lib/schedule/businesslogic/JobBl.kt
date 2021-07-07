/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.businesslogic

import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.data.StatusUpdate
import zakadabar.lib.schedule.data.Subscription
import zakadabar.lib.schedule.persistenceapi.JobExposedPa
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.module
import zakadabar.stack.data.builtin.ActionStatusBo

/**
 * Business Logic for Job.
 *
 * @param   adminRole        Role for: list, read, update, delete.
 * @param   createRole       Role for: create
 * @param   dispatcherRole   Role for: Poll, UpdateStatus
 */
open class JobBl(
    private val adminRole: String,
    private val createRole : String,
    private val dispatcherRole: String
) : EntityBusinessLogicBase<Job>(
    boClass = Job::class
) {

    override val pa = JobExposedPa()

    open val subscriptionBl by module<SubscriptionBl>()

    override val authorizer = SimpleRoleAuthorizer<Job> {
        all = adminRole
        update = createRole
        action(StatusUpdate::class, dispatcherRole)
    }

    override val router = router {
        action(StatusUpdate::class, ::updateStatus)
    }

    override fun create(executor: Executor, bo: Job): Job {
        val created = super.create(executor, bo)
        subscriptionBl.addJob(created)
        return created
    }

    open fun subscribe(executor: Executor, action: Subscription) : ActionStatusBo {
        TODO()
    }

    open fun updateStatus(executor: Executor, action: StatusUpdate): ActionStatusBo {
        TODO()
    }

}