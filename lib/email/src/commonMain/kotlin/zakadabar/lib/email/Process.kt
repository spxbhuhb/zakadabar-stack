/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.EntityId

/**
 * Processes a mail. This is the actual send that connects to the SMTP server
 * and sends the mail.
 *
 * This action is not node-safe. It is intended to be called from a job scheduler.
 *
 * @param  mail  Id of the mail to send.
 */
@Serializable
class Process(
    var mail: EntityId<Mail>
) : ActionBo<Unit> {

    override suspend fun execute(executor: Executor?, callConfig: CommConfig?) =
        comm.action(this, serializer(), Unit.serializer(), executor, callConfig)

    companion object : ActionBoCompanion(Mail.boNamespace)

}