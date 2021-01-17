/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.builtin.AccountPublic
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

@Serializable
class AccountPublicDto(

    override var id: Long,
    override var accountName: String,
    override var email: String,
    override var fullName: String,
    override var displayName: String,
    override var organizationName: String

) : RecordDto<AccountPublicDto>, AccountPublic {

    companion object : RecordDtoCompanion<AccountPublicDto>({
        recordType = "account-public"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    // No need for schema for this as this is basically a view of AccountPrivateDto

}