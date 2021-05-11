/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import zakadabar.stack.backend.Forbidden
import zakadabar.stack.backend.Server.Companion.anonymous
import zakadabar.stack.backend.Server.Companion.staticRoot
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.session.*
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.backend.ktor.session.*
import zakadabar.stack.backend.routingLogger
import zakadabar.stack.data.DataConflictException
import zakadabar.stack.data.builtin.settings.ServerSettingsDto
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.util.Executor
import java.io.File
import java.time.Duration

fun buildServer(
    config: ServerSettingsDto,
    recordBackends: List<RecordBackend<*>>,
    customBackends: List<CustomBackend>
) = embeddedServer(Netty, port = config.ktor.port) {

    install(Sessions) {
        val sessionType = StackSession::class
        val name = "STACK_SESSION"

        @Suppress("DEPRECATION") // as in Ktor code
        val builder = CookieIdSessionBuilder(sessionType).apply {
            cookie.path = "/"
        }
        val transport = SessionTransportCookie(name, builder.cookie, builder.transformers)
        val tracker = RenewableSessionTrackerById(sessionType, StackSessionSerializer, SessionStorageSql, builder.sessionIdProvider)
        val provider = SessionProvider(name, sessionType, transport, tracker)
        register(provider)

        SessionMaintenanceTask.start()
    }

    install(Authentication) {
        session()

        basic(name = "basic") {
            realm = config.serverName
            validate {
                val (account, principalId) = SessionBackend.authenticate(LongRecordId(anonymous.id.toLong()), it.name, it.password) ?: return@validate null
                val (roleIds, roleNames) = PrincipalBackend.roles(principalId)
                return@validate Executor(LongRecordId(account.id.toLong()), roleIds, roleNames)
            }
        }

    }

    install(ContentNegotiation) {
        json(Json { })
    }

    install(Compression) {

    }

    install(WebSockets) {
        val c = config.ktor.websocket
        pingPeriod = Duration.ofSeconds(c.pingPeriod)
        timeout = Duration.ofSeconds(c.timeout)
        maxFrameSize = c.maxFrameSize
        masking = c.masking
    }

    install(StatusPages) {
        exception<LoginTimeout> {
            call.respond(HttpStatusCode(440, "Login Timeout"))
        }
        exception<Forbidden> {
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<EntityNotFoundException> {
            call.respond(HttpStatusCode.NotFound)
        }
        exception<DataConflictException> {
            call.respond(HttpStatusCode.Conflict, it.message)
        }
        status(HttpStatusCode.NotFound) {
            val uri = call.request.uri
            // this check is here so we will redirect only when needed
            // api not found has to go directly to the browser, check
            // for index.html is there to avoid recursive redirection
            if (! uri.startsWith("/api") && uri != "/index.html") {
                call.respondFile(File(staticRoot, "/index.html"))
            }
        }
    }

    routing {
        if (config.traceRouting) trace { routingLogger.trace(it.buildText()) }

        authenticate {

            get("health") {
                call.respondText("OK", ContentType.Text.Plain)
            }

            route("api") {

                // api installs add routes and the code to serve them
                recordBackends.forEach {
                    it.onInstallRoutes(this)
                }

                customBackends.forEach {
                    it.onInstallRoutes(this)
                }

                get("health") {
                    call.respondText("OK", ContentType.Text.Plain)
                }

            }

            static {
                staticRootFolder = File(config.staticResources)
                files(".")
                default("index.html")

                // api installs add routes and the code to serve them
                recordBackends.forEach {
                    it.onInstallStatic(this)
                }

                customBackends.forEach {
                    it.onInstallStatic(this)
                }
            }

        }

    }
}
