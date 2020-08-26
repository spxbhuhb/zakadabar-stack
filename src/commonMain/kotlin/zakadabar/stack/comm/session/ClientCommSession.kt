/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.comm.session

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import zakadabar.stack.comm.message.CloseStackSessionRequest
import zakadabar.stack.comm.message.OpenStackSessionRequest
import zakadabar.stack.comm.serialization.OutputCommArray
import zakadabar.stack.comm.serialization.ResponseCode
import zakadabar.stack.util.PublicApi

/**
 * Base class for transfer sessions on the client side. The connection is opened at the time
 * the instance is created.
 */
open class ClientCommSession<RQ, RS>(
    private val host: String,
    private val port: Int,
    private val path: String,
    private val openRequest: () -> RQ,
    private val onOpenResponse: (RS) -> Unit,
    private val closeRequest: () -> RQ,
    private val write: (request: RQ) -> OutputCommArray,
    private val read: (response: ByteArray) -> RS
) {

    private val channel = Channel<Pair<RQ, CompletableDeferred<RS>>>()

    private val client = HttpClient { install(WebSockets) }

    private lateinit var job: Job

    /**
     * This lock to makes sure there are no pending messages in the channel.
     * It is also used for startup synchronization, it will be unlocked by [open].
     */
    private val lock = Mutex(true)

    private var open = false

    /**
     * Opens the session. Connects to the server, authenticates the user
     * and waits for tasks to execute.
     *
     * [authToken] is called after the websocket connection is built to get the
     * authentication token to send in [OpenStackSessionRequest].
     */
    suspend fun open() {
        val response = try {
            // decided to go with GlobalScope here, this is conceptually a background communication
            // thread so it is actually a "daemon thread"

            job = GlobalScope.launch {
                this@ClientCommSession.run()
            }

            val deferredResponse = CompletableDeferred<RS>()

            channel.send(openRequest() to deferredResponse)

            val response = deferredResponse.await()

            open = true

            response

        } finally {
            // it doesn't matter how open ended, we have release the lock so sends go
            // on, worst case they'll return with an error
            lock.unlock()
        }

        onOpenResponse(response)

    }

    /**
     * Send a message and receive a response.
     */
    suspend fun send(builder: () -> RQ): RS {
        val deferredResponse = CompletableDeferred<RS>()

        lock.withLock {
            if (open) {
                channel.send(builder() to deferredResponse)
            } else {
                throw SessionError(ResponseCode.SESSION_IS_CLOSED)
            }
        }

        return deferredResponse.await()
    }

    /**
     * Sends a [CloseStackSessionRequest] to the server and when the response
     * arrives closes the session. This is graceful close, tasks added before
     * [close] are executed.
     */
    @PublicApi
    suspend fun close() {
        send { closeRequest() }
        channel.close()
        job.join()
    }

    /**
     * Closes the session immediately, no communication to the server is performed,
     * the session job is cancelled. Waits util the job actually finishes.
     */
    @PublicApi
    suspend fun abort() {
        lock.withLock { open = false }
        job.cancelAndJoin()
    }

    /**
     * Provides an authentication token to send to the server.
     */
    open fun authToken(): String = "" // FIXME auth token

    /**
     * Called when there is an error during the session lifetime. This
     * includes communication errors, authentication errors, messaging errors.
     */
    open fun onError(ex: Throwable) {
        throw ex
    }

    private suspend fun run() {
        try {

            client.ws(method = HttpMethod.Get, host = host, port = port, path = path) {

                for ((request, deferred) in channel) {
                    try {

                        send(Frame.Binary(true, write(request).pack()))

                        val frame = incoming.receive()

                        when (frame.frameType) {
                            FrameType.BINARY -> deferred.complete(read(frame.data))
                            else -> throw IllegalStateException()
                        }

                    } catch (ex: Throwable) {
                        deferred.completeExceptionally(ex)
                        onError(ex)
                        break
                    }
                }

            }

        } catch (ex: Throwable) {

            onError(ex)

        } finally {

            lock.withLock { open = false }

            for ((_, deferredResponse) in channel) {
                deferredResponse.completeExceptionally(SessionError(ResponseCode.SESSION_IS_CLOSED))
            }

        }
    }
}