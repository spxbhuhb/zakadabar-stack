/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.action.nullresult

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.StringValue
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.UUID

@Serializable
class Action(
    var returnValue : String?
) : ActionBo<StringValue?> {

    override suspend fun execute() = comm.actionOrNull(this, serializer(), StringValue.serializer())

    companion object : ActionBoCompanion(UUID().toString())

    override fun schema() = BoSchema {
        + ::returnValue
    }
}