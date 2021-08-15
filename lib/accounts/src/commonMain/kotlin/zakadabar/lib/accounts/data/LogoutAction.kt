/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.ActionStatus

@Serializable
class LogoutAction : ActionBo<ActionStatus> {

    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    companion object : ActionBoCompanion(SessionBo.boNamespace)

}