/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.accounts.backend

import zakadabar.lib.accounts.data.*
import zakadabar.stack.authorize.appRoles
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.RoleBlProvider
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer.Companion.LOGGED_IN
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId

open class RoleBl : RoleBlProvider, EntityBusinessLogicBase<RoleBo>(
    boClass = RoleBo::class
) {

    override val pa = RoleExposedPa()

    override val authorizer = SimpleRoleAuthorizer<RoleBo> {
        all = appRoles.securityOfficer
        action(GrantRole::class, appRoles.securityOfficer)
        action(RevokeRole::class, appRoles.securityOfficer)
        query(AccountsByRole::class, LOGGED_IN)
        query(RolesByAccount::class, appRoles.securityOfficer)
    }

    override val router = router {
        action(GrantRole::class, ::grantRole)
        action(RevokeRole::class, ::revokeRole)
        query(AccountsByRole::class, ::accountsByRole)
        query(RolesByAccount::class, ::rolesByAccount)
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    private fun rolesByAccount(executor: Executor, query: RolesByAccount) =
        pa.rolesByAccount(query.accountId)

    private fun accountsByRole(executor: Executor, query: AccountsByRole) =
        pa.accountsByRole(pa.readByName(query.roleName).id)

    // -------------------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------------------

    internal fun grantRole(executor: Executor, grant: GrantRole): ActionStatusBo {
        pa.grant(RoleGrantBo(grant.account, grant.role))
        return ActionStatusBo()
    }

    private fun revokeRole(executor: Executor, revoke: RevokeRole): ActionStatusBo {
        pa.revoke(RoleGrantBo(revoke.account, revoke.role))
        return ActionStatusBo()
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    fun rolesOf(accountId: EntityId<AccountPrivateBl>) = pa.withTransaction {
        pa.rolesOf(accountId)
    }

    override fun getByName(name: String) = pa.withTransaction {
        pa.readByName(name).id
    }


}