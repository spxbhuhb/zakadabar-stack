/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import io.ktor.auth.*
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

/**
 * Ktor authentication principal id.
 *
 * @property  accountId  The id of the Principal entity this principal id points to.
 */
open class Executor internal constructor(

    val accountId: EntityId<out BaseBo>,
    val anonymous: Boolean,
    val roleIds: List<EntityId<out BaseBo>>,
    private val roleNames: List<String>

) : Principal {

    fun hasRole(roleName: String) = roleName in roleNames

    fun hasRole(roleId: EntityId<out BaseBo>) = roleId in roleIds

    fun hasOneOfRoles(roleNames: Array<out String>): Boolean {
        roleNames.forEach {
            if (it in roleNames) return true
        }
        return false
    }

}