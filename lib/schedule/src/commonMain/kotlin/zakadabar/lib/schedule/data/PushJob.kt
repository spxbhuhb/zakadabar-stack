/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

/**
 * Used by the dispatcher to push a job to a worker. If the worker accepts the job
 * it should return with a successful action status. If it cannot accept the job
 * for any reasons it may return with an unsuccessful action status.
 */
@Serializable
class PushJob(

    var jobId: EntityId<Job>,
    var actionNamespace: String,
    var actionType: String,
    var actionData: String

) : ActionBo<Unit> {

    companion object : ActionBoCompanion(Job.boNamespace)

    override suspend fun execute(executor : Executor?, callConfig : CommConfig?) =
        comm.action(this, serializer(), Unit.serializer(), executor, callConfig)

    override fun schema() = BoSchema {
        + ::jobId
        + ::actionNamespace
        + ::actionType
        + ::actionData
    }

}