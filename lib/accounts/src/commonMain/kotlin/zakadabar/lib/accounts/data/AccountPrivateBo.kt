/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(OptInstantAsStringSerializer::class)

package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema
import zakadabar.stack.data.schema.Formats
import zakadabar.stack.data.util.OptInstantAsStringSerializer

@Serializable
class AccountPrivateBo(

    override var id: EntityId<AccountPrivateBo>,

    var principal: EntityId<PrincipalBo>,

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

) : EntityBo<AccountPrivateBo> {

    companion object : EntityBoCompanion<AccountPrivateBo>("account-private")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
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