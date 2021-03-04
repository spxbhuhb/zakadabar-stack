/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import io.ktor.auth.*

/**
 * Ktor authentication principal id.
 *
 * @property  accountId  The id of the Principal entity this principal id points to.
 */
open class Executor internal constructor(

    val accountId: Long,
    private val roles: List<String>

) : Principal {

    fun hasRole(roleName: String) = roleName in roles

    fun oneOf(roleNames: Array<out String>): Boolean {
        roleNames.forEach {
            if (it in roles) return true
        }
        return false
    }

}