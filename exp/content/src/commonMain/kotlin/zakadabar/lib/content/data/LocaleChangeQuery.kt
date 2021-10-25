/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.data.StringValue

/**
 * Query the new SEO path when the locale changes.
 */
@Serializable
class LocaleChangeQuery(
    val fromLocale: String,
    val fromPath : String,
    val toLocale : String
): QueryBo<StringValue> {

    companion object : QueryBoCompanion(ContentBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), StringValue.serializer())

}