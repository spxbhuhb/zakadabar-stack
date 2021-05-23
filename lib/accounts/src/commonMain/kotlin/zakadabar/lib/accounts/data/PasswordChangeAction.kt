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
 * Password change data. Account id is present to allow administrators change the
 * password of other accounts.
 */
@Serializable
class PasswordChangeAction(

    var accountId: EntityId<AccountPrivateBo>,
    var oldPassword: Secret,
    var newPassword: Secret

) : ActionBo<ActionStatusBo> {

    companion object : ActionBoCompanion<PasswordChangeAction>(PrincipalBo.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    override fun schema() = BoSchema {
        + ::accountId
        + ::oldPassword blank false
        + ::newPassword blank false min 8
    }

}