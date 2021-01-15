/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.schema.DtoSchema

@Serializable
class AccountDto(

    override var id: Long,
    var accountName: String,
    var email: String,
    var fullName: String,
    var displayName: String?,
    var organizationName: String?,
    var avatar: Long?

) : RecordDto<AccountDto> {

    companion object : RecordDtoCompanion<AccountDto>({
        recordType = "account"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::accountName min 3 blank false
        + ::email min 5 blank false
        + ::fullName min 3 blank false
        + ::displayName min 3 blank false
        + ::organizationName
        + ::avatar
    }
}