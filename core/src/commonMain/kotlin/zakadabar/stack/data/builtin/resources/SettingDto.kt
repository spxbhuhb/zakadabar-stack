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

@Serializable
data class SettingDto(
    override var id: RecordId<SettingDto>,
    var role: RecordId<RoleDto>?,
    var source: SettingSource,
    var namespace: String,
    var className: String,
    var descriptor: String?
) : RecordDto<SettingDto> {

    companion object : RecordDtoCompanion<SettingDto>({
        namespace = "setting"
    })

    override fun getRecordType() = namespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::role
        + ::source
        + ::namespace min 0 max 100
        + ::className min 1 max 100 blank false
    }
}