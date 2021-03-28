/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.resources

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

/**
 * A general configuration setting.
 *
 * @property  id      Id of the configuration setting record.
 * @property  name    Name of the setting.
 * @property  value   Value of the setting.
 */
@Serializable
data class SettingStringDto(
    override var id: RecordId<SettingStringDto>,
    var name: String,
    var value: String,
) : RecordDto<SettingStringDto> {

    companion object : RecordDtoCompanion<SettingStringDto>({
        recordType = "setting-string"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::name min 1 max 100 blank false
        + ::value
    }
}