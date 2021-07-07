/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * Update status of the job. Used by workers to send status to the dispatcher
 * after/during job execution.
 */
@Serializable
class StatusUpdate(

    var jobId : EntityId<Job>,
    var status : JobStatus,
    var progress : Double,
    var responseData : String

) : ActionBo<ActionStatusBo> {

    companion object : ActionBoCompanion(Job.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    override fun schema() = BoSchema {
        + ::jobId
        + ::status
        + ::progress min 0.0 max 100.0
        + ::responseData
    }

}