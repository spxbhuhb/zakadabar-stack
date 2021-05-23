/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.accounts.backend

import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.data.entity.EntityBusinessLogicBase
import zakadabar.stack.data.builtin.account.RoleGrantBo

class RoleGrantBl : EntityBusinessLogicBase<RoleGrantBo>() {

    override val boClass = RoleGrantBo::class

    override val pa = RoleGrantExposedPa()

    override val authorizer = SimpleRoleAuthorizer<RoleGrantBo> {
        list = StackRoles.securityOfficer
        read = StackRoles.securityOfficer
        create = StackRoles.securityOfficer
        update = StackRoles.securityOfficer
        delete = StackRoles.securityOfficer
    }

}