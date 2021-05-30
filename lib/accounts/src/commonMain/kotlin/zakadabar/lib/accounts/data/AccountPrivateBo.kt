/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
class AccountPrivateBo(

    override var id: EntityId<AccountPrivateBo>,

    var validated : Boolean,
    var locked : Boolean,
    var expired : Boolean,
    var credentials : Secret?,
    var resetKey : Secret?,
    var resetKeyExpiration : Instant?,
    var lastLoginSuccess : Instant?,
    var loginSuccessCount : Int,
    var lastLoginFail : Instant?,
    var loginFailCount : Int,

    var accountName: String,
    var fullName: String,
    var email: String,
    var phone: String?,

    var displayName: String?,
    var theme: String?,
    var locale: String

) : EntityBo<AccountPrivateBo> {

    companion object : EntityBoCompanion<AccountPrivateBo>("zkl-account-private")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id

        + ::validated
        + ::locked
        + ::expired
        + ::credentials
        + ::resetKey
        + ::resetKeyExpiration
        + ::lastLoginSuccess
        + ::loginSuccessCount
        + ::lastLoginFail
        + ::loginFailCount

        + ::accountName min 3 max 50
        + ::fullName min 5 max 100
        + ::email min 4 max 50
        + ::phone min 10 max 20

        + ::displayName min 3 max 50
        + ::locale max 20
        + ::theme max 50
    }
}