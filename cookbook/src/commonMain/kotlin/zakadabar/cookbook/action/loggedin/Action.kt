/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.action.loggedin

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.util.UUID

@Serializable
class Action : ActionBo<Long> {

    override suspend fun execute() = comm.query(this, serializer(), Long.serializer())

    // TODO change the namespace
    companion object : QueryBoCompanion(UUID().toString())

}
