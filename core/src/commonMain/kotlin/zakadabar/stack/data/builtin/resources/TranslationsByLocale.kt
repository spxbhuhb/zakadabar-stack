/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.resources

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion

/**
 * Get all strings for the given locale
 */
@Serializable
data class TranslationsByLocale(
    var locale: String
) : QueryDto<TranslationDto> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(TranslationDto.serializer()))

    companion object : QueryDtoCompanion<TranslationDto>(TranslationDto.namespace)

}