/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.security

import kotlinx.serialization.Serializable

/**
 * Get common account ids. Used to get the display name and avatar that belongs
 * to specific account ids.
 */
@Serializable
class SearchAccounts(
    val accountIds: List<Long>
) {
    suspend fun execute() = CommonAccountDto.comm.query(this, serializer())
}