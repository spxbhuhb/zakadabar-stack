/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class RoleGrantDto(

    override var id: Long,
    var principal: RecordId<PrincipalDto>,
    var role: RecordId<RoleDto>

) : RecordDto<RoleGrantDto> {

    companion object : RecordDtoCompanion<RoleGrantDto>({
        recordType = "role-grant"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::principal
        + ::role
    }

}