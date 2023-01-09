/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.comm

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

fun httpClientWithAuth(username : String, password: String) = HttpClient {
    install(Auth) {
        basic {
            credentials { BasicAuthCredentials(username, password) }
            sendWithoutRequest { true }
        }
    }
}