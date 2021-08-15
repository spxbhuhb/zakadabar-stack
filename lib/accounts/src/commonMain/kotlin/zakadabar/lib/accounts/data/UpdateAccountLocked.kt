/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.ActionStatus
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

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

) : ActionBo<ActionStatus> {

    companion object : ActionBoCompanion(AccountPrivateBo.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    override fun schema() = BoSchema {
        + ::accountId
        + ::locked
    }


}