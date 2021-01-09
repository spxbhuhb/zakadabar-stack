/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

interface AccountSummary {

    val id: Long
    val accountName: String
    val emailAddress: String
    val fullName: String
    val displayName: String?
    val organizationName: String?
    val avatar: Long?

    val isAnonymous
        get() = (accountName == "anonymous")
}