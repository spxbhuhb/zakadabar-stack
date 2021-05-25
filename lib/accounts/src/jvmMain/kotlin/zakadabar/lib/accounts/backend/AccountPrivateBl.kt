/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.lib.accounts.data.AccountPublicBo
import zakadabar.lib.accounts.data.PrincipalBo
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.Forbidden
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.data.entity.EntityBusinessLogicBase
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor

class AccountPrivateBl : EntityBusinessLogicBase<AccountPrivateBo>(
    boClass = AccountPrivateBo::class
) {

    override val pa = AccountPrivateExposedPaGen()

    override val authorizer = object : SimpleRoleAuthorizer<AccountPrivateBo>({
        list = StackRoles.securityOfficer
        allWrites = StackRoles.securityOfficer
    }) {
        override fun authorizeRead(executor: Executor, entityId: EntityId<AccountPrivateBo>) {
            if (executor.accountId == entityId || executor.hasRole(StackRoles.securityOfficer)) return
            throw Forbidden()
        }
    }

    fun findAccountById(accountId: EntityId<AccountPrivateBo>): Pair<AccountPublicBo,EntityId<PrincipalBo>> {
        TODO("Not yet implemented")
    }

}