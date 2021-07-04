/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * Action to create a new account. This is fundamentally different than
 * a CRUD create on ActionPrivateBo.
 */
@Serializable
class CreateAccount(

    var credentials : Secret?,

    var accountName: String,
    var fullName: String,
    var email: String,
    var phone: String?,

    var theme: String?,
    var locale: String,

    var validated : Boolean,
    var locked : Boolean,

    var roles: List<EntityId<RoleBo>>

) : ActionBo<ActionStatusBo> {

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    companion object : ActionBoCompanion(AccountPrivateBo.boNamespace)

    override fun schema() = BoSchema {
        + ::credentials

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