/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.action.nullresult

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.IntValue
import zakadabar.stack.data.schema.BoSchema
import zakadabar.stack.util.UUID

@Serializable
class Action(
    var returnValue : Long?
) : ActionBo<IntValue?> {

    override suspend fun execute() = comm.actionOrNull(this, serializer(), IntValue.serializer())

    // TODO change the namespace
    companion object : ActionBoCompanion(UUID().toString())

    override fun schema() = BoSchema {
        + ::returnValue
    }
}
