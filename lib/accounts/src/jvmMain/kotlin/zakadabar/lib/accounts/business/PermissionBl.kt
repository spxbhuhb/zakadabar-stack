/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.accounts.business

import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.PermissionBlProvider
import zakadabar.lib.accounts.data.*
import zakadabar.core.authorize.appRoles
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.ActionStatus
import zakadabar.core.data.EntityId
import zakadabar.core.setting.setting
import zakadabar.lib.accounts.persistence.PermissionExposedPa

open class PermissionBl : PermissionBlProvider, EntityBusinessLogicBase<PermissionBo>(
    boClass = PermissionBo::class
) {

    private val settings by setting<ModuleSettings>()

    override val pa = PermissionExposedPa()

    override val authorizer = SimpleRoleAuthorizer<PermissionBo> {
        all = appRoles.securityOfficer
        query(PermissionsByRole::class, LOGGED_IN)
    }

    override val router = router {
        action(GrantPermission::class, ::grantPermission)
        action(RevokePermission::class, ::revokePermission)
        query(PermissionsByRole::class, ::permissionsByRole)
    }
//
//    // -------------------------------------------------------------------------
//    // Queries
//    // -------------------------------------------------------------------------
//
    private fun permissionsByRole(executor: Executor, query: PermissionsByRole) =
        pa.byRole(query.roleId)

//    // -------------------------------------------------------------------------
//    // Actions
//    // -------------------------------------------------------------------------

    internal fun grantPermission(executor: Executor, action: GrantPermission): ActionStatus {
        pa.addPermission(RolePermissionBo(action.role, action.permission))
        return ActionStatus()
    }

    private fun revokePermission(executor: Executor, action: RevokePermission): ActionStatus {
        pa.removePermission(RolePermissionBo(action.role, action.permission))
        return ActionStatus()
    }

//    // -------------------------------------------------------------------------
//    // Internal
//    // -------------------------------------------------------------------------

    fun permissionsOf(accountId: EntityId<AccountPrivateBl>) = pa.withTransaction {
        pa.permissionsOf(accountId)
    }

    override fun getByName(name: String) = pa.withTransaction {
        pa.readByName(name).id
    }


}