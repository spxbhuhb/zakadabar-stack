/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.resources

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import zakadabar.stack.data.builtin.account.RoleGrantDto
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion
import zakadabar.stack.data.schema.DtoSchema

/**
 * Get all strings for the given locale
 */
@Serializable
data class StringsByLocale(
    var locale: String
) : QueryDto<LocaleStringDto> {

    override suspend fun execute() = comm().query(this, serializer(), ListSerializer(LocaleStringDto.serializer()))

    companion object : QueryDtoCompanion<LocaleStringDto>()

}