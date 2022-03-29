/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.serialization.Serializable
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.UUID

/**
 * Settings for a worker.
 *
 * @param  workerId         The ID of the node that creates this subscription.
 * @param  scheduleAccount  Account for worker - dispatcher communication.
 * @param  dispatcherComm   Communication configuration to reach the dispatcher.
 * @param  workerComm       Communication configuration to reach the worker.
 * @param  actionNamespace  Filters the jobs this subscription applies to.
 * @param  actionType       Filters the jobs this subscription applies to.
 */
@Serializable
class WorkerSettings(

    var workerId: UUID = UUID(),
    val scheduleAccount: String,
    var dispatcherComm: CommConfig = CommConfig(local = true),
    var workerComm : CommConfig = CommConfig(local = true),
    var actionNamespace: String? = null,
    var actionType: String? = null

) : BaseBo {

    override fun schema() = BoSchema.NO_VALIDATION

}