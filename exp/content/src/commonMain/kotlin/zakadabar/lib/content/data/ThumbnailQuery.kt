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
 * Query thumbnail images and titles of children of a given parent.
 */
@Serializable
class ThumbnailQuery(
    val localeName : String,
    val parent : EntityId<ContentBo>
): QueryBo<List<ThumbnailEntry>> {

    companion object : QueryBoCompanion(ContentBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(ThumbnailEntry.serializer()))

}

/**
 * An entry returned by folder query.
 */
@Serializable
class ThumbnailEntry(
    val masterId : EntityId<ContentBo>,
    val localizedId : EntityId<ContentBo>,
    val title : String,
    val seoPath : String,
    var thumbnailImageUrl: String?
) : BaseBo