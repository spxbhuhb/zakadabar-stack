/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import zakadabar.lib.accounts.data.RoleGrantBo
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase


class RoleGrantBl : EntityBusinessLogicBase<RoleGrantBo>(
    boClass = RoleGrantBo::class
) {

    override val pa = RoleGrantExposedPaGen()

    override val authorizer = SimpleRoleAuthorizer<RoleGrantBo> {
        list = StackRoles.siteMember
        read = StackRoles.siteMember
        create = StackRoles.securityOfficer
        update = StackRoles.securityOfficer
        delete = StackRoles.securityOfficer
    }

}