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
import zakadabar.stack.data.util.OptInstantAsStringSerializer

@Serializable
class PrincipalBo(

    override var id: EntityId<PrincipalBo>,
    var validated : Boolean,
    var locked : Boolean,
    var expired : Boolean,
    var credentials : Secret?,
    var resetKey : Secret?,
    @Serializable(OptInstantAsStringSerializer::class)
    var resetKeyExpiration : Instant?,
    @Serializable(OptInstantAsStringSerializer::class)
    var lastLoginSuccess : Instant?,
    var loginSuccessCount : Int,
    @Serializable(OptInstantAsStringSerializer::class)
    var lastLoginFail : Instant?,
    var loginFailCount : Int

) : EntityBo<PrincipalBo> {

    companion object : EntityBoCompanion<PrincipalBo>("zkl-principal")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
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
    }

}