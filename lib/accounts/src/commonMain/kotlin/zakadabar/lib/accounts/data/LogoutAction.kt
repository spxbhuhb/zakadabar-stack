/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.ActionStatusBo

@Serializable
class LogoutAction : ActionBo<ActionStatusBo> {

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    companion object : ActionBoCompanion<ActionStatusBo>(SessionBo.boNamespace)

}