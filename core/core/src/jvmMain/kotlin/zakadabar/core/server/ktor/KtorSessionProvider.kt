/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

import io.ktor.server.auth.*
import io.ktor.server.sessions.*

interface KtorSessionProvider {
    fun configure(conf : SessionsConfig)
    fun configure(conf : AuthenticationConfig)
}