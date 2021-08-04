/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId

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
    var mail : EntityId<Mail>
) : ActionBo<ActionStatusBo> {

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    companion object : ActionBoCompanion(Mail.boNamespace)

}