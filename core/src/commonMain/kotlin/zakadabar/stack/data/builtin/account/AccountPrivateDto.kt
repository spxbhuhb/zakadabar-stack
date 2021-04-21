/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(OptInstantAsStringSerializer::class)

package zakadabar.stack.data.builtin.account

import kotlinx.serialization.*
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema
import zakadabar.stack.data.schema.Formats
import zakadabar.stack.data.util.OptInstantAsStringSerializer

@Serializable
data class AccountPrivateDto(

    override var id: RecordId<AccountPrivateDto>,

    val principal: RecordId<PrincipalDto>,

    var accountName: String,
    var fullName: String,
    var email: String,

    var displayName: String?,
    var theme: String?,
    var locale: String?,
    var avatar: Long?,

    var organizationName: String?,
    var position: String?,
    var phone: String?

) : RecordDto<AccountPrivateDto> {

    companion object : RecordDtoCompanion<AccountPrivateDto>({
        recordType = "account-private"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id

        + ::accountName min 3 max 50
        + ::fullName min 5 max 100
        + ::email min 4 max 50 format Formats::email

        + ::displayName min 3 max 50
        + ::locale max 20
        + ::theme max 50
        + ::avatar

        + ::organizationName min 2 max 100
        + ::position min 3 max 50
        + ::phone min 10 max 20
    }
}