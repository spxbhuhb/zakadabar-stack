/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId

@Serializable
data class RoleGrantDto(

    override val id: Long,
    val accountId: RecordId<AccountSummaryDto>,
    val roleId: RecordId<RoleDto>

) : RecordDto<RoleGrantDto> {

    companion object : RecordDtoCompanion<RoleGrantDto>() {
        override val recordType = "${Stack.shid}/role-grant"
    }

    override fun getRecordType() = recordType

    override fun comm() = comm

}