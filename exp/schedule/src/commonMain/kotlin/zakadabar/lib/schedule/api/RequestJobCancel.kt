/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.api

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.ActionStatus
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

/**
 * Requests a job cancellation. Successful execution of this action does not
 * mean that the job has been cancelled, as that might not be possible.
 *
 * When this action is executed, the dispatcher is notifier that this job
 * should be cancelled.
 *
 * If the job is pending or waiting for a worker the cancellation is immediate.
 *
 * If a worker already works on this job the dispatcher will send this request
 * to the worker and the worker decides if cancellation is possible or not.
 *
 * If cancellation is possible, the worker interrupts the job and executes
 * the [JobCancel] action to let the scheduler know that this job was indeed
 * cancelled.
 */
@Serializable
class RequestJobCancel(

    var jobId : EntityId<Job>

) : ActionBo<ActionStatus> {

    companion object : ActionBoCompanion(Job.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    override fun schema() = BoSchema {
        + ::jobId
    }

}