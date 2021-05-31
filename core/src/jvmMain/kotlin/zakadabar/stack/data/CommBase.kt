/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

import io.ktor.client.*
import io.ktor.client.features.cookies.*
import zakadabar.stack.util.PublicApi

@PublicApi
open class CommBase {

    companion object {
        lateinit var baseUrl: String

        val client = HttpClient {
            install(HttpCookies) {
                // Will keep an in-memory map with all the cookies from previous requests.
                storage = AcceptAllCookiesStorage()
            }
        }

        /**
         * Called then Ktor client throws an exception.
         */
        var onError: suspend (ex: Exception) -> Unit = { }
    }

}