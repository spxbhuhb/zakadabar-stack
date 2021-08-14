/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.accounts.backend.bl

import zakadabar.lib.accounts.backend.RoleExposedPa
import zakadabar.lib.accounts.data.*
import zakadabar.core.authorize.appRoles
import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.RoleBlProvider
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.authorize.SimpleRoleAuthorizer.Companion.LOGGED_IN
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.setting.setting
import zakadabar.core.data.builtin.ActionStatusBo
import zakadabar.core.data.entity.EntityId

open class RoleBl : RoleBlProvider, EntityBusinessLogicBase<RoleBo>(
    boClass = RoleBo::class
) {

    private val settings by setting<ModuleSettings>()

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
        pa.accountsByRole(
            pa.readByName(query.roleName).id,
            withEmail = settings.emailInAccountPublic,
            withPhone = settings.phoneInAccountPublic,
        )

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