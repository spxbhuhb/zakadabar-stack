/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * Updates the locked status of the account. When switching from locked
 * to unlocked, also resets number of login fails to let the user log in.
 *
 * @param   accountId   Id of the account entity to change.
 * @param   locked      When true locks the account, when false unlocks it.
 */
@Serializable
class UpdateAccountLocked(

    var accountId : EntityId<AccountPrivateBo>,
    var locked : Boolean

) : ActionBo<ActionStatusBo> {

    companion object : ActionBoCompanion(AccountPrivateBo.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    override fun schema() = BoSchema {
        + ::accountId
        + ::locked
    }


}