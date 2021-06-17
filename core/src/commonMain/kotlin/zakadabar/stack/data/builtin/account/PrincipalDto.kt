/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(OptInstantAsStringSerializer::class)

package zakadabar.stack.data.builtin.account

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema
import zakadabar.stack.data.util.OptInstantAsStringSerializer

/**
 * Represents a principal in the system. Usually it is tied to an account.
 * the reason of storing the principal independent from the account is that
 * the account usually contains extended information that depends on the
 * actual business function. On the other hand, principal stores general
 * information related to authentication and authorization.
 */
@Serializable
data class PrincipalDto(

    override var id: RecordId<PrincipalDto>,

    var validated: Boolean,
    var locked: Boolean,
    var expired: Boolean,
    var lastLoginSuccess: Instant?,
    var loginSuccessCount: Int,
    var lastLoginFail: Instant?,
    var loginFailCount: Int

) : RecordDto<PrincipalDto> {

    companion object : RecordDtoCompanion<PrincipalDto>("principal")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id

        // + ::validated
        + ::locked
        + ::expired

        + ::loginSuccessCount
        + ::lastLoginSuccess

        + ::loginFailCount
        + ::lastLoginFail
    }
}