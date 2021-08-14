/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

import io.ktor.auth.*
import io.ktor.sessions.*

interface KtorSessionProvider {
    fun configure(conf : Sessions.Configuration)
    fun configure(conf : Authentication.Configuration)
}