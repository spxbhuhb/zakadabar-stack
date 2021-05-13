/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(OptInstantAsStringSerializer::class)

package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema
import zakadabar.stack.data.schema.Formats
import zakadabar.stack.data.util.OptInstantAsStringSerializer

@Serializable
data class AccountPrivateDto(

    override var id: RecordId<AccountPrivateDto>,

    var principal: RecordId<PrincipalDto>,

    var accountName: String,
    var fullName: String,
    var email: String,

    var displayName: String?,
    var theme: String?,
    var locale: String,
    var avatar: Long?,

    var organizationName: String?,
    var position: String?,
    var phone: String?

) : RecordDto<AccountPrivateDto> {

    companion object : RecordDtoCompanion<AccountPrivateDto>("account-private")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id

        + ::principal

        + ::accountName min 3 max 50
        + ::fullName min 5 max 100
        + ::email min 4 max 50 format Formats.EMAIL

        + ::displayName min 3 max 50
        + ::locale max 20
        + ::theme max 50
        + ::avatar

        + ::organizationName min 2 max 100
        + ::position min 3 max 50
        + ::phone min 10 max 20
    }
}