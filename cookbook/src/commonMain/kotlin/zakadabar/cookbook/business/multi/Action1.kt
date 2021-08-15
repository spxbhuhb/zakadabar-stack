/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.multi

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.LongValue
import zakadabar.core.util.UUID

@Serializable
class Action1 : ActionBo<LongValue> {

    override suspend fun execute() = comm.action(this, serializer(), LongValue.serializer())

    // TODO change the namespace
    companion object : ActionBoCompanion(UUID().toString())

}
