/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.IntValue
import zakadabar.stack.data.schema.BoSchema

@Serializable
class Action(
    var returnValue : Long?
) : ActionBo<IntValue?> {

    override suspend fun execute() = comm.actionOrNull(this, serializer(), IntValue.serializer())

    // TODO change the namespace
    companion object : ActionBoCompanion("test-action")

    override fun schema() = BoSchema {
        + ::returnValue
    }
}