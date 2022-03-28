/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.serialization.Serializable
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.data.StringValue
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.UUID

@Serializable
class TestQuery(
    var returnValue : String
) : QueryBo<StringValue> {

    override suspend fun execute() = comm.query(this, serializer(), StringValue.serializer())

    companion object : QueryBoCompanion(UUID().toString())

    override fun schema() = BoSchema {
        + ::returnValue
    }
}