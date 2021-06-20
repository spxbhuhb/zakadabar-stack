/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

/**
 * Ktor authentication principal id.
 *
 * @property  accountId  The id of the account this executor describes.
 * @property  anonymous  True when this executor is the anonymous account (public, not logged in).
 * @property  roleIds    List of the ids of the roles this account has.
 * @property  roleNames  List of the names of the roles this account has.
 * @property  locale     Id of the locale this account prefers.
 */
open class Executor(

    val accountId: EntityId<out BaseBo>,
    val anonymous: Boolean,
    val roleIds: List<EntityId<out BaseBo>>,
    val roleNames: List<String>,
    val locale: String

) {

    fun hasRole(roleName: String) = roleName in roleNames

    fun hasRole(roleId: EntityId<out BaseBo>) = roleId in roleIds

    fun hasOneOfRoles(roleNames: Array<out String>): Boolean {
        roleNames.forEach {
            if (it in roleNames) return true
        }
        return false
    }

}