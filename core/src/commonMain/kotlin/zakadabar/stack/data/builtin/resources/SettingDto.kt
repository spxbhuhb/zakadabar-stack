/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.resources

import kotlinx.serialization.Serializable
import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

/**
 * A general configuration setting.
 *
 * @property  id      Id of the configuration setting record.
 * @property  role    Id of the role this setting string is visible for. When null everyone can see the setting.
 * @property  name    Name of the setting.
 * @property  value   Value of the setting.
 */
@Serializable
data class SettingDto(
    override var id: RecordId<SettingDto>,
    var role: RecordId<RoleDto>?,
    var format: SettingFormat,
    var namespace: String,
    var path: String,
    var value: String,
) : RecordDto<SettingDto> {

    companion object : RecordDtoCompanion<SettingDto>({
        namespace = "setting"
    })

    override fun getRecordType() = namespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::role min 1
        + ::format default SettingFormat.TEXT
        + ::namespace min 0 max 100
        + ::path min 1 max 100 blank false
        + ::value
    }
}