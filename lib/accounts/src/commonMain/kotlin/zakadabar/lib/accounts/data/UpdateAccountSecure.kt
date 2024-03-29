/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.ActionStatus
import zakadabar.core.data.Secret
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

/**
 * Changes e-mail and/or phone number of an account. Security officers are allowed to
 * change any account without supplying the password. Exception is their own account
 * which can be changed only if the password is provided and correct.
 *
 * This update is password protected because many cases password reset function
 * is linked to these fields.
 *
 * @param  accountId     The id of the account entity to change the email of.
 * @param  password      The current password of the account.
 * @param  email         The new email of the account.
 * @param  phone         The new phone number of the account.
 */
@Serializable
class UpdateAccountSecure(

    var accountId: EntityId<AccountPrivateBo>,
    var password: Secret,
    var email: String,
    var phone: String

) : ActionBo<ActionStatus> {

    companion object : ActionBoCompanion(AccountPrivateBo.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    override fun schema() = BoSchema {
        + ::accountId
        + ::password blank false
        + ::email min 4 max 50
        + ::phone min 10 max 20
    }

}