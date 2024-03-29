/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.UUID

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
 *  @param  worker           The worker this job runs or will run on.
 *  @param  failCount        Number of failed run attempts.
 *  @param  retryCount       Number of retry attempts to perfom if the job fails.
 *  @param  retryInterval    Interval between retries, seconds.
 *  @param  lastFailData     JSON data that belongs to the fail policy and contains
 *                           details about the last failure.
 *  @param  deleteOnSuccess  Delete this job if it finishes successfully.
 *  @param  progress         For a long-running job the progress of the job (percent).
 *  @param  responseData     Response data as received from the action execution.
 *  @param  origin           Origin of the job. For example, the id of the schedule that generated this job.
 */
@Serializable
class Job(

    override var id : EntityId<Job>,
    var status : JobStatus,
    var createdBy : UUID,
    var createdAt : Instant,
    var startAt : Instant?,
    var completedAt : Instant?,
    var specific : Boolean,
    var actionNamespace : String,
    var actionType : String,
    var actionData : String,
    var worker : UUID?,
    var failCount: Int,
    var retryCount: Int,
    var retryInterval: Int,
    var lastFailedAt: Instant?,
    var lastFailMessage : String?,
    var lastFailData : String?,
    var deleteOnSuccess : Boolean,
    var progress : Double,
    var progressText : String?,
    var lastProgressAt : Instant?,
    var responseData : String?,
    var origin: String?

) : EntityBo<Job> {

    companion object : EntityBoCompanion<Job>("zk-schedule-job")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::status default JobStatus.Pending
        + ::createdBy
        + ::createdAt
        + ::startAt
        + ::completedAt
        + ::specific
        + ::actionNamespace blank false min 1 max 100
        + ::actionType blank false min 1 max 100
        + ::actionData blank false
        + ::worker
        + ::failCount
        + ::retryCount
        + ::retryInterval
        + ::lastFailedAt
        + ::lastFailMessage
        + ::lastFailData
        + ::deleteOnSuccess
        + ::progress min 0.0 max 100.0
        + ::progressText
        + ::lastProgressAt
        + ::responseData
        + ::origin
    }

}