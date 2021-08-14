/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.action.ActionBoCompanion
import zakadabar.core.data.builtin.ActionStatusBo
import zakadabar.core.data.builtin.misc.Secret
import zakadabar.core.data.schema.BoSchema

/**
 * Action BO for login.
 */
@Serializable
class LoginAction(

    var accountName: String,
    var password: Secret

) : ActionBo<ActionStatusBo> {

    /**
     * Executes the login.
     *
     * @throws  Unauthorized  When the login fails. Data of unauthorized shows if
     *                        the login failed because the account is locked, the
     *                        account does not have the necessary role to log in
     *                        with this action, or for other reason.
     */
    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    companion object : ActionBoCompanion(SessionBo.boNamespace)

    override fun schema() = BoSchema {
        + ::accountName min 1 max 50 blank false
        + ::password min 1 max 50 blank false
    }

}