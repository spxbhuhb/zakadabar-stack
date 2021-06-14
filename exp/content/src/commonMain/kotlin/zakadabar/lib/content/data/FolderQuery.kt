/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

/**
 * Query folders. Folders are masters with the `folder` flag on true.
 */
@Serializable
class FolderQuery: QueryBo<List<FolderEntry>> {

    companion object : QueryBoCompanion(ContentBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(FolderEntry.serializer()))

}

/**
 * An entry returned by folder query.
 */
@Serializable
class FolderEntry(
    val id : EntityId<ContentBo>,
    val title : String
) : BaseBo