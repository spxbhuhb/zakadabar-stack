/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.resources

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

/**
 * A string in a given locale.
 *
 * @property  id      Id of the string locale record.
 * @property  locale  The locale of the string like "hu-HU".
 * @property  name    Name of the string resource as defined in the string store.
 * @property  value   Value of the string resource.
 */
@Serializable
data class TranslationDto(
    override var id: RecordId<TranslationDto>,
    var name: String,
    var locale: String,
    var value: String,
) : RecordDto<TranslationDto> {

    companion object : RecordDtoCompanion<TranslationDto>("translation")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::name min 1 max 100 blank false
        + ::locale min 2 max 20 blank false
        + ::value
    }
}