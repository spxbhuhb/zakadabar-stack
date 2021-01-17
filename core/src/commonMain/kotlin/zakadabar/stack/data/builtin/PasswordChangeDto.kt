/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

/**
 * Password change data. Account id is present to allow administrators change the
 * password of other accounts.
 */
@Serializable
data class PasswordChangeDto(

    override var id: RecordId<PasswordChangeDto>,
    var accountId: Long,
    var oldPassword: String,
    var newPassword: String

) : RecordDto<PasswordChangeDto> {

    companion object : RecordDtoCompanion<PasswordChangeDto>({
        recordType = "password-change"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::oldPassword
        + ::newPassword
    }

}