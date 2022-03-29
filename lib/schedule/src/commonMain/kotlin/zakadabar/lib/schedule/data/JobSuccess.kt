/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.serialization.Serializable
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.ActionStatus
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

@Serializable
class JobSuccess(

    var jobId : EntityId<Job>,
    var responseData : String?

) : ActionBo<ActionStatus> {

    companion object : ActionBoCompanion(Job.boNamespace)

    override suspend fun execute(executor: Executor?, callConfig : CommConfig?) =
        comm.action(this, serializer(), ActionStatus.serializer(), executor, callConfig)

    override fun schema() = BoSchema {
        + ::jobId
        + ::responseData
    }

}