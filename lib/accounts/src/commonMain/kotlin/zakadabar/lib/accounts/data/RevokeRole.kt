/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.action.ActionBoCompanion
import zakadabar.core.data.builtin.ActionStatusBo
import zakadabar.core.data.entity.EntityId

/**
 * Revoke a role from an account.
 *
 * @param  account   The account to revoke the role from.
 * @param  role      The role to revoke.
 */
@Serializable
class RevokeRole(

    var account : EntityId<AccountPrivateBo>,
    var role : EntityId<RoleBo>

) : ActionBo<ActionStatusBo> {

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    companion object : ActionBoCompanion(RoleBo.boNamespace)

}
