/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(OptInstantAsStringSerializer::class)

package zakadabar.stack.data.builtin.account

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema
import zakadabar.stack.data.util.OptInstantAsStringSerializer

/**
 * Represents a principal in the system. Usually it is tied to an account.
 * the reason of storing the principal independent from the account is that
 * the account usually contains extended information that depends on the
 * actual business function. On the other hand, principal stores general
 * information related to authentication and authorization.
 */
@Serializable
class PrincipalBo(

    override var id: EntityId<PrincipalBo>,

    var validated: Boolean,
    var locked: Boolean,
    var expired: Boolean,
    var lastLoginSuccess: Instant?,
    var loginSuccessCount: Int,
    var lastLoginFail: Instant?,
    var loginFailCount: Int

) : EntityBo<PrincipalBo> {

    companion object : EntityBoCompanion<PrincipalBo>("principal")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
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