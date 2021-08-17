/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

/**
 * Query folders. Folders are masters with the `folder` flag on true.
 */
@Serializable
class MastersQuery: QueryBo<List<MastersEntry>> {

    companion object : QueryBoCompanion(ContentBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(MastersEntry.serializer()))

}

/**
 * An entry returned by folder query.
 */
@Serializable
class MastersEntry(
    val id : EntityId<ContentBo>,
    val title : String
) : BaseBo