/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

import io.ktor.http.*
import kotlinx.coroutines.await
import org.w3c.fetch.Response
import zakadabar.stack.frontend.application.ZkApplication.sessionManager
import zakadabar.stack.frontend.builtin.pages.account.login.RenewLoginDialog

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
            sessionManager.renew()
        }
    }

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
            }
        }
    }

    /**
     * Checks the HTTP status code of the response and throws exceptions
     * as needed.
     */
    suspend fun checkStatus(response: Response): Response {
        val code = response.status.toInt()
        when {
            code == HttpStatusCode.Conflict.value -> throw DataConflictException(response.text().await())
            code == 440 -> throw LoginTimeout()
            code >= 400 -> throw RuntimeException()
        }
        return response
    }
}