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
 * Changes the password of an account. Security officers are allowed to change any
 * passwords without supplying the old password. Exception is their own account
 * which can be changed only if the old password is provided and correct.
 *
 * @param  accountId     The id of the account entity to change the password of.
 * @param  oldPassword   The current password of the account.
 * @param  newPassword   The new password of the account.
 */
@Serializable
class PasswordChange(

    var accountId: EntityId<AccountPrivateBo>,
    var oldPassword: Secret,
    var newPassword: Secret

) : ActionBo<ActionStatusBo> {

    companion object : ActionBoCompanion(AccountPrivateBo.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    override fun schema() = BoSchema {
        + ::accountId
        + ::oldPassword blank false
        + ::newPassword blank false min 8
    }

}