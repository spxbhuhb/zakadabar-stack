/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.session

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import zakadabar.demo.data.account.LoginDto
import zakadabar.stack.backend.data.CustomBackend
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.builtin.SessionDto

object SessionBackend : CustomBackend() {

    override fun install(route: Route) {
        with(route) {
            route(SessionDto.recordType) {

                // the frontend calls
                get("0") {
                    val session = call.sessions.get<SessionDto>() ?: throw IllegalStateException()
                    call.respond(session)
                }

                patch("login") {
                    val executor = call.executor()
                    val request = call.receive(LoginDto::class)

                    val old = call.sessions.get<SessionDto>() !!
                    // TODO perform the actual login
                    val new = old.copy(accountId = 0L)

                    logger.info("${executor.entityId}: LOGIN session=${old.id} account=${request.account}")

                    call.sessions.set(new)
                    call.respond(new)
                }

                patch("logout") {
                    val executor = call.executor()

                    val old = call.sessions.get<SessionDto>() !!
                    val new = old.copy(accountId = 0L, displayName = "Anonymous", roles = listOf("anonymous"))

                    logger.info("${executor.entityId}: LOGOUT session=${old.id}")

                    call.sessions.set(new)
                    call.respond(new)
                }

            }
        }
    }

}