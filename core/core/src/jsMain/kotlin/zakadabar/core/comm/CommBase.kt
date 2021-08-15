/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.coroutines.await
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import zakadabar.core.exception.DataConflict
import zakadabar.core.exception.Unauthorized
import zakadabar.core.exception.UnauthorizedData
import zakadabar.core.frontend.application.application
import zakadabar.core.frontend.builtin.toast.toastDanger
import zakadabar.core.resource.localizedStrings

/**
 * Common functions for all comm classes.
 */
abstract class CommBase {

    companion object {
        /**
         * Called then the server responds with HTTP status 440 Login Timeout.
         * The default implementation creates and runs a [RenewLoginDialog].
         */
        var onLoginTimeout: suspend () -> Unit = {
            application.sessionManager.renew()
        }

        /**
         * Called then the server responds with HTTP status 403 Forbidden.
         * Default implementation displays a toast.
         */
        var onForbidden: suspend () -> Unit = {
            toastDanger { localizedStrings.forbiddenExplanation }
        }

        /**
         * Called then the server responds with HTTP status 4xx and 5xx
         * error codes that are not 440 or 403. Default implementation displays a toast.
         */
        var onError : suspend () -> Unit = {
            toastDanger { localizedStrings.executeError }
        }
    }

    class Forbidden : Exception()
    class LoginTimeout : Exception()

    /**
     * Executes a block (usually containing a fetch) and if the block
     * throws a [LoginTimeout] calls [onLoginTimeout].
     *
     * All server communication should be wrapped in a [commBlock] or
     * preceded directly by a [commBlock].
     */
    suspend fun commBlock(block: suspend () -> Response): Response {
        while (true) {
            try {
                return block()
            } catch (ex: LoginTimeout) {
                onLoginTimeout()
            } catch (ex : Forbidden) {
                onForbidden()
                throw ex
            } catch (ex : Exception) {
                onError()
                throw ex
            }
        }
    }

    /**
     * Checks the HTTP status code of the response and throws exceptions
     * as needed.
     */
    suspend fun checkStatus(response: Response): Response {
        val code = response.status.toInt()

        if (code >= 400) {
            when (code) {
                400 -> throw IllegalArgumentException()
                401 -> throw Unauthorized(data = Json.decodeFromString(UnauthorizedData.serializer(), response.text().await()))
                403 -> throw Forbidden()
                404 -> throw NoSuchElementException()
                409 -> throw DataConflict(response.text().await())
                440 -> throw LoginTimeout()
                501 -> throw NotImplementedError()
                else -> throw RuntimeException()
            }
        }

        return response
    }
}