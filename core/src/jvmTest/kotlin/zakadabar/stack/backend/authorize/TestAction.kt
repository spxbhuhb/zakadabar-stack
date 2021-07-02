/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion

@Serializable
class TestAction : ActionBo<TestAction> {
    override suspend fun execute(): TestAction {
        throw NotImplementedError("this is just a test action")
    }

    companion object : ActionBoCompanion("not-used")
}