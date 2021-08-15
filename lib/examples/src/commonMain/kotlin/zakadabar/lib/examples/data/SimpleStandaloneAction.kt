/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.examples.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.ActionStatus
import zakadabar.core.schema.BoSchema

@Serializable
class SimpleStandaloneAction(

    var name : String

) : ActionBo<ActionStatus> {

    companion object : ActionBoCompanion("zkl-simple-standalone-action")

    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    override fun schema() = BoSchema {
        + ::name blank false min 1 max 30
    }

}