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
 * A locale like "hu-HU".
 *
 * @property  id            Id of the locale record.
 * @property  name          Name of the locale.
 * @property  description   Description of the locale.
 */
@Serializable
data class LocaleDto(
    override var id: RecordId<LocaleDto>,
    var name: String,
    var description: String
) : RecordDto<LocaleDto> {

    companion object : RecordDtoCompanion<LocaleDto>("locale")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::name min 2 max 100 blank false
        + ::description
    }
}