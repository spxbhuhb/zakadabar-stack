/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

/**
 * Query contents by SEO titles. This query provides resolution for
 * URLs like: `/hu/megoldások/ipari-automatizálás`. This is in Hungarian
 * and we have to know which content it points to. The same URL in English
 * is `/en/solutions/industrial-automation`. The two points two different,
 * localized content entities with the same master.
 *
 * @param  path               The path to resolve.
 */
@Serializable
class BySeoPath(
    val path : String
): QueryBo<ContentBo> {

    companion object : QueryBoCompanion(ContentBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ContentBo.serializer())

}