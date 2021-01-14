/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId

@Serializable
data class RoleGrantDto(

    override var id: Long,
    val account: RecordId<AccountDto>,
    val role: RecordId<RoleDto>

) : RecordDto<RoleGrantDto> {

    companion object : RecordDtoCompanion<RoleGrantDto>() {
        override val recordType = "role-grant"
    }

    override fun getRecordType() = recordType

    override fun comm() = comm

}