/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.business.query.anonymous

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.util.UUID

@Serializable
class Query : QueryBo<List<String>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(String.serializer()))

    // TODO change the namespace
    companion object : QueryBoCompanion(UUID().toString())

}
