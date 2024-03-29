/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.query.nullresult

import kotlinx.serialization.Serializable
import zakadabar.core.data.StringValue
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.UUID

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