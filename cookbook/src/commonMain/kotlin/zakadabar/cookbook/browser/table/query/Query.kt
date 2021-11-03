/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.query

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.util.UUID

@Serializable
class Query : QueryBo<List<QueryResultEntry>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(QueryResultEntry.serializer()))

    companion object : QueryBoCompanion(UUID().toString())

}

@Serializable
class QueryResultEntry(
    var entityId: EntityId<ExampleBo>,
    var field1: String,
    var field2: String,
) : BaseBo