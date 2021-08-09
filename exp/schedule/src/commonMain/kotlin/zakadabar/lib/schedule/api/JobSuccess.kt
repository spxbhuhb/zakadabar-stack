/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.api

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
class JobSuccess(

    var jobId : EntityId<Job>,
    var responseData : String?

) : ActionBo<ActionStatusBo> {

    companion object : ActionBoCompanion(Job.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    override fun schema() = BoSchema {
        + ::jobId
        + ::responseData
    }

}