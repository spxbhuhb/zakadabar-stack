/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.StackRoles
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

object MockRoleBlProvider : BackendModule, RoleBlProvider {
    override fun getByName(name: String): EntityId<BaseBo> = when (name) {
        StackRoles.anonymous -> EntityId(1)
        StackRoles.siteMember -> EntityId(2)
        StackRoles.siteAdmin -> EntityId(3)
        StackRoles.securityOfficer -> EntityId(4)
        else -> throw NoSuchElementException()
    }
}