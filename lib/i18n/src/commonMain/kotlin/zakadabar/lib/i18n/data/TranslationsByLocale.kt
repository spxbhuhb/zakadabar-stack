/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.StringPair
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

/**
 * Get all strings for the given locale
 */
@Serializable
class TranslationsByLocale(
    var locale: String
) : QueryBo<List<StringPair>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(StringPair.serializer()))

    companion object : QueryBoCompanion(TranslationBo.boNamespace)

}