/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

/**
 * Query the possible SEO path for any given path.
 */
@Serializable
class LocaleOptionsQuery(
    val fromLocale: String,
    val fromPath : String
): QueryBo<List<LocaleAndPath>> {

    companion object : QueryBoCompanion(ContentBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(LocaleAndPath.serializer()))

}