/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.builtin.misc.StringPair
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

/**
 * Get all strings for the given locale
 */
@Serializable
class TranslationsByLocale(
    var locale: String
) : QueryBo<StringPair> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(StringPair.serializer()))

    companion object : QueryBoCompanion<StringPair>(TranslationBo.boNamespace)

}