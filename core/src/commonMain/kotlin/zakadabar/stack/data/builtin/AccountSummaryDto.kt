/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable

/**
 * Summary of an account. Intended for selects which contains list of
 * accounts.
 */
@Serializable
data class AccountSummaryDto(

    val id: Long = 0,
    val name: String = "",
    val emailAddress: String = "",
    val fullName: String = "",
    val displayName: String = "",
    val organizationName: String = "",
    val avatar: Long? = null

) {
    val isAnonymous
        get() = (name == "anonymous")
}