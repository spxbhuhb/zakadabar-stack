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
 * Query navigation links from a given starting point.
 */
@Serializable
class NavQuery(
    val localeName : String,
    val from : EntityId<ContentBo>?
): QueryBo<List<NavEntry>> {

    companion object : QueryBoCompanion(ContentBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(NavEntry.serializer()))

}

/**
 * An entry returned by folder query.
 */
@Serializable
class NavEntry(
    val masterId : EntityId<ContentBo>,
    val localizedId : EntityId<ContentBo>,
    val title : String,
    val seoPath : String,
    val folder: Boolean,
    var children : List<NavEntry>
) : BaseBo