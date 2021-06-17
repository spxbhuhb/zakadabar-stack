/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.examples.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.schema.BoSchema

@Serializable
class SimpleStandaloneAction(

    var name : String

) : ActionBo<ActionStatusBo> {

    companion object : ActionBoCompanion("zkl-simple-standalone-action")

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    override fun schema() = BoSchema {
        + ::name blank false min 1 max 30
    }

}