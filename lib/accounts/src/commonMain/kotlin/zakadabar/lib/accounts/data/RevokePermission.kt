/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.ActionStatus
import zakadabar.core.data.EntityId

/**
 * Revoke a role from an account.
 *
 * @param  account   The account to revoke the role from.
 * @param  role      The role to revoke.
 */
@Serializable
class RevokePermission(

    var role : EntityId<RoleBo>,
    var permission: EntityId<PermissionBo>

) : ActionBo<ActionStatus> {

    override suspend fun execute() = comm.action(this, serializer(), ActionStatus.serializer())

    companion object : ActionBoCompanion(PermissionBo.boNamespace)

}
