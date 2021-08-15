/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.ActionStatus
import zakadabar.core.data.Secret
import zakadabar.core.schema.BoSchema

/**
 * Action BO for login.
 */
@Serializable
class LoginAction(

    var accountName: String,
    var password: Secret

) : ActionBo<ActionStatus> {

    /**
     * Executes the login.
     *
     * @throws  Unauthorized  When the login fails. Data of unauthorized shows if
     *                        the login failed because the account is locked, the
     *                        account does not have the necessary role to log in
     *                        with this action, or for other reason.
     */
    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    companion object : ActionBoCompanion(SessionBo.boNamespace)

    override fun schema() = BoSchema {
        + ::accountName min 1 max 50 blank false
        + ::password min 1 max 50 blank false
    }

}