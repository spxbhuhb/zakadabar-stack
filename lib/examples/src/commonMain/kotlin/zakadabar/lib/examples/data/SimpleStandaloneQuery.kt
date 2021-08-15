/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.examples.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.schema.BoSchema

@Serializable
class SimpleStandaloneQuery(

    var name : String

) : QueryBo<List<SimpleQueryResult>> {

    companion object : QueryBoCompanion("zkl-simple-standalone-query")

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(SimpleQueryResult.serializer()))

    override fun schema() = BoSchema {
        + ::name blank false min 1 max 30
    }

}