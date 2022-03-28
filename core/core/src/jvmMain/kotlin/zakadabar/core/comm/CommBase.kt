/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import io.ktor.client.*
import io.ktor.client.features.cookies.*
import zakadabar.core.comm.CommConfig.Companion.global
import zakadabar.core.util.PublicApi

@PublicApi
open class CommBase {

    companion object {
        @Deprecated("use commConfig instead")
        var baseUrl
            get() = global.baseUrl
            set(value) {
                global = global.copy(baseUrl = value)
            }

        var client = httpClient()

        /**
         * Called then Ktor client throws an exception.
         */
        var onError: suspend (ex: Exception) -> Unit = { }

        @PublicApi
        fun httpClient() = HttpClient {
            install(HttpCookies) {
                // Will keep an in-memory map with all the cookies from previous requests.
                storage = AcceptAllCookiesStorage()
            }
        }



    }

}