/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.serialization.Serializable
import zakadabar.core.authorize.Executor
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.StringValue
import zakadabar.core.schema.BoSchema

@Serializable
class TestActionNull(
    var returnValue : String?
) : ActionBo<StringValue?> {

    override suspend fun execute() = comm.actionOrNull(this, serializer(), StringValue.serializer())

    override suspend fun execute(executor: Executor?, callConfig: CommConfig?) =
        comm.actionOrNull(this, serializer(), StringValue.serializer(), executor, callConfig)

    companion object : ActionBoCompanion(TestBo.boNamespace)

    override fun schema() = BoSchema {
        + ::returnValue
    }
}