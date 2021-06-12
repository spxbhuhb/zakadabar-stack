/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

/**
 * Query contents by localized properties. This query provides resolution for
 * URLs like: `/hu/megoldások/ipari-automatizálás`. This is an URL in Hungarian
 * and we have to know which content it points to. The same URL in English
 * is `/en/solutions/industrial-automation`. The two points to the same content.
 *
 * @param  localeName               Name of the locale.
 * @param  localizedStereotypeName  Localized name of the stereotype.
 * @param  localizedContentTitle    Localized name of the content.
 */
@Serializable
class ContentByStereotypeAndTitle(
    val localeName : String,
    val localizedStereotypeName : String,
    val localizedContentTitle: String
): QueryBo<ContentBo> {

    companion object : QueryBoCompanion(ContentBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ContentBo.serializer())

}