/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import io.ktor.client.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*

fun httpClientWithAuth(username : String, password: String) = HttpClient {
    install(Auth) {
        basic {
            credentials { BasicAuthCredentials(username, password) }
            sendWithoutRequest { true }
        }
    }
}