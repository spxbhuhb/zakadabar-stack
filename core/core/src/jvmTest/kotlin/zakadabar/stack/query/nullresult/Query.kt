/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.query.nullresult

import kotlinx.serialization.Serializable
import zakadabar.stack.data.builtin.StringValue
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.data.schema.BoSchema
import zakadabar.stack.util.UUID

@Serializable
class Query(
    var returnValue : String?
) : QueryBo<StringValue?> {

    override suspend fun execute() = comm.queryOrNull(this, serializer(), StringValue.serializer())

    companion object : QueryBoCompanion(UUID().toString())

    override fun schema() = BoSchema {
        + ::returnValue
    }
}