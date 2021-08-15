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
 * Sent by the worker to Job BL to inform about a successful job cancellation.
 */
@Serializable
class JobCancel(

    var jobId : EntityId<Job>

) : ActionBo<ActionStatus> {

    companion object : ActionBoCompanion(Job.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    override fun schema() = BoSchema {
        + ::jobId
    }

}