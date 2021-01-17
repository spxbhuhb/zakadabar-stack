/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

/**
 * An interface account DTO should implement to provide access to core
 * functions to the commonly used account data.
 */
interface AccountPublic {

    val id: Long
    val accountName: String
    val email: String
    val fullName: String
    val displayName: String
    val organizationName: String

}