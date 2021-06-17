/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.examples.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.data.schema.BoSchema

@Serializable
class SimpleExampleQuery(

    var name : String

) : QueryBo<List<SimpleQueryResult>> {

    companion object : QueryBoCompanion(SimpleExampleBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(SimpleQueryResult.serializer()))

    override fun schema() = BoSchema {
        + ::name blank false min 1 max 30
    }

}