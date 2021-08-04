/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema
import zakadabar.stack.util.UUID

/**
 *  @param  id               Id of the job (UUID).
 *  @param  status           Status of the job.
 *  @param  createdAt        Creation time of this job entity.
 *  @param  completedAt      Completion time of this job entity.
 *  @param  startAt          Desired start time of this job. When null the job
 *                           may be started immediately.
 *  @param  specific         When true, this job can be sent to subscribers which
 *                           specifically process this namespace and/or action type.
 *  @param  actionNamespace  Namespace of the action that performs this job.
 *  @param  actionType       Type of the action that performs this job.
 *  @param  actionData       JSON data of the action that performs this job.
 *  @param  node             The node this job runs or will run on.
 *  @param  failPolicy       Specifies what should happen when the job fails.
 *  @param  failCount        Number of failed run attempts.
 *  @param  failData         JSON data that belongs to the fail policy and contains
 *                           details about the last failure.
 *  @param  deleteOnSuccess  Delete this job if it finishes successfully.
 *  @param  progress         For a long running job the progress of the job (percent).
 *  @param  responseData     Response data as received from the action execution.
 */
@Serializable
class Job(

    override var id : EntityId<Job>,
    var status : JobStatus,
    var createdAt : Instant,
    var completedAt : Instant?,
    var startAt : Instant?,
    var specific : Boolean,
    var actionNamespace : String,
    var actionType : String,
    var actionData : String,
    var node : UUID?,
    var failPolicy : String?,
    var failCount: Int,
    var failData : String?,
    var deleteOnSuccess : Boolean,
    var progress : Double,
    var responseData : String?

) : EntityBo<Job> {

    companion object : EntityBoCompanion<Job>("zk-schedule-job")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::status
        + ::createdAt
        + ::completedAt
        + ::startAt
        + ::specific
        + ::actionNamespace blank false min 1 max 100
        + ::actionType blank false min 1 max 100
        + ::actionData blank false
        + ::node
        + ::failPolicy blank false max 100
        + ::failCount
        + ::failData blank false
        + ::deleteOnSuccess
        + ::progress min 0.0 max 100.0
        + ::responseData blank false
    }

}