/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.query.Queries
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class AccountDto(

    override var id: Long = 0,

    var emailAddress: String = "",
    var fullName: String = "",
    var displayName: String = "",
    var organizationName: String = "",
    var avatar: Long? = null,
    var password: String = ""

) : RecordDto<AccountDto> {

    companion object : RecordDtoCompanion<AccountDto>() {

        override val recordType = "${Stack.shid}/account"

        override val queries = Queries.build {
            + SearchAccounts
        }
    }

    override fun schema() = DtoSchema.build {
        + ::emailAddress max 100
        + ::fullName min 2 max 100
        + ::displayName max 20
        + ::organizationName max 100
    }

    override fun getRecordType() = recordType

    override fun comm() = comm

}