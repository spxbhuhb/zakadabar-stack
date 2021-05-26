/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.resources

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

/**
 * Get all strings for the given locale
 */
@Serializable
data class TranslationsByLocale(
    var locale: String
) : QueryBo<TranslationBo> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(TranslationBo.serializer()))

    companion object : QueryBoCompanion<TranslationBo>(TranslationBo.boNamespace)

}