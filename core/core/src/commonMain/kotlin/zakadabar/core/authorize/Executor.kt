/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.util.UUID

/**
 * Represents an entity that executes system functions.
 *
 * @property  accountId    The id of the account this executor describes.
 * @property  accountUuid  The UUID of the account this executor describes.
 * @property  anonymous    True when this executor is the anonymous account (public, not logged in).
 * @property  roleIds      List of the ids of the roles this account has.
 * @property  roleNames    List of the names of the roles this account has.
 * @property  isLoggedIn   True when the executor is logged in (not anonymous).
 */
open class Executor(

    val accountId: EntityId<out BaseBo>,
    val accountUuid : UUID,
    val anonymous: Boolean,
    val roleIds: List<EntityId<out BaseBo>>,
    val roleNames: List<String>,
    val permissionIds: List<EntityId<out BaseBo>>,
    val permissionNames: List<String>

    ) {

    val isLoggedIn = ! anonymous

    fun hasRole(roleName: String) = roleName in roleNames

    fun hasRole(roleId: EntityId<out BaseBo>) = roleId in roleIds

    fun hasOneOfRoles(roleNames: Array<out String>): Boolean {
        roleNames.forEach {
            if (it in this.roleNames) return true
        }
        return false
    }

    fun hasPermission(permissionName: String) = permissionName in permissionNames

    fun hasPermission(permissionId: EntityId<out BaseBo>) = permissionId in permissionIds

    fun hasOneOfPermissions(permissionNames: Array<out String>): Boolean {
        permissionNames.forEach {
            if (it in this.permissionNames) return true
        }
        return false
    }

}