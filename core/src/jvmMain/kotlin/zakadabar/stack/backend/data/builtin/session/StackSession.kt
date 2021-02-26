/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.session

/**
 * Session data class passed to Ktor.
 *
 * @property account Id of the account this session belongs to.
 */
data class StackSession(
    val account: Long,
    val roles: List<String>
)