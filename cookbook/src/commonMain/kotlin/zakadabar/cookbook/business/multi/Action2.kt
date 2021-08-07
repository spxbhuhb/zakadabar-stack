/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.multi

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.LongValue
import zakadabar.stack.util.UUID

@Serializable
class Action2 : ActionBo<LongValue> {

    override suspend fun execute() = comm.action(this, serializer(), LongValue.serializer())

    // TODO change the namespace
    companion object : ActionBoCompanion(UUID().toString())

}
