/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

@Serializable
class AccountDto(

    override val id: Long = 0,
    val accountName: String = "",
    val email: String = "",
    val fullName: String = "",
    val displayName: String? = null,
    val organizationName: String? = null,
    val avatar: Long? = null

) : RecordDto<AccountDto> {

    companion object : RecordDtoCompanion<AccountDto>() {
        override val recordType = "account"
    }

    override fun getRecordType() = recordType

    override fun comm() = comm

}