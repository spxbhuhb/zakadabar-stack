/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.*
import zakadabar.core.schema.BoSchema

/**
 * Action to create a new account. This is fundamentally different than
 * a CRUD create on ActionPrivateBo.
 */
@Serializable
class CreateAccount(

    var credentials : Secret,

    var accountName: String,
    var fullName: String,
    var email: String,
    var phone: String?,

    var theme: String?,
    var locale: String,

    var validated : Boolean,
    var locked : Boolean,

    var roles: List<EntityId<RoleBo>>

) : ActionBo<ActionStatus> {

    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    companion object : ActionBoCompanion(AccountPrivateBo.boNamespace)

    override fun schema() = BoSchema {
        + ::credentials min 8

        + ::accountName min 2 max 50
        + ::fullName min 5 max 100
        + ::email min 4 max 50
        + ::phone min 10 max 20

        + ::locale max 20
        + ::theme max 50

        + ::validated
        + ::locked
    }


}