/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.accounts.backend

import zakadabar.lib.accounts.data.RoleBo
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.RoleBlProvider
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.data.entity.EntityBusinessLogicBase

class RoleBl(
) : RoleBlProvider, EntityBusinessLogicBase<RoleBo>(
    boClass = RoleBo::class
) {

    override val pa = RoleExposedPa()

    override val authorizer = SimpleRoleAuthorizer<RoleBo> {
        list = StackRoles.siteMember
        read = StackRoles.siteMember
        create = StackRoles.securityOfficer
        update = StackRoles.securityOfficer
        delete = StackRoles.securityOfficer
    }

    override fun getByName(name: String) = pa.readByName(name).id

}