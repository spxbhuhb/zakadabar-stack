/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

class Executor(
    val accountId: Long,
    val displayName: String,
    val roles: List<String>
) {
    val isAnonymous = roles.isEmpty()
}