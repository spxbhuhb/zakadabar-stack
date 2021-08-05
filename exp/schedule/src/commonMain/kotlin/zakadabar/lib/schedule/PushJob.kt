/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * Used by the dispatcher to push a job to a worker. If the worker accepts the job
 * it should return with a successful action status. If it cannot accept the job
 * for any reasons it may return with an unsuccessful action status.
 */
@Serializable
class PushJob(

    var jobId : EntityId<Job>,
    var actionNamespace : String,
    var actionType : String,
    var actionData : String,
    var failPolicy : String?,
    var failCount: Int,
    var failData : String?,

    ) : ActionBo<ActionStatusBo> {

    companion object : ActionBoCompanion(Job.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    override fun schema() = BoSchema {
        + ::jobId
        + ::actionNamespace
        + ::actionType
        + ::actionData
        + ::failPolicy
        + ::failCount
        + ::failData
    }

}