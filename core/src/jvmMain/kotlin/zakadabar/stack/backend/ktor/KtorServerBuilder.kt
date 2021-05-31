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
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server.Companion.staticRoot
import zakadabar.stack.backend.authorize.Forbidden
import zakadabar.stack.backend.authorize.LoginTimeout
import zakadabar.stack.backend.routingLogger
import zakadabar.stack.backend.server
import zakadabar.stack.data.DataConflictException
import zakadabar.stack.data.builtin.settings.ServerSettingsBo
import zakadabar.stack.util.InstanceStore
import java.io.File
import java.time.Duration

/**
 * Build a Ktor server instance.
 */
open class KtorServerBuilder(
    open val config: ServerSettingsBo,
    open val modules: InstanceStore<BackendModule>
) {

    fun build() = embeddedServer(Netty, port = config.ktor.port) {

        install(ContentNegotiation) {
            json(Json { })
        }

        install(Compression) {

        }

        session()
        websockets()
        statusPages()
        routing()

    }

    open fun Application.session() {
        val sessionBl = server.firstOrNull<KtorSessionProvider>()

        if (sessionBl != null) {
            install(Sessions) {
                sessionBl.configure(this)
            }

            install(Authentication) {
                sessionBl.configure(this)
            }
        } else {
            install(Authentication) {
                configure()
            }
        }
    }

    open fun Application.statusPages() {
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
    }

    open fun Application.websockets() {
        install(WebSockets) {
            val c = config.ktor.websocket
            pingPeriod = Duration.ofSeconds(c.pingPeriod)
            timeout = Duration.ofSeconds(c.timeout)
            maxFrameSize = c.maxFrameSize
            masking = c.masking
        }
    }

    open fun Application.routing() {
        routing {
            if (config.traceRouting) trace { routingLogger.trace(it.buildText()) }

            authenticate {
                install(config, modules)
            }

        }
    }

    open fun Route.install(config: ServerSettingsBo, modules: InstanceStore<BackendModule>) {

        get("health") {
            call.respondText("OK", ContentType.Text.Plain)
        }

        route("api") {

            modules.instances.forEach {
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

            modules.instances.forEach {
                it.onInstallStatic(this)
            }

        }
    }

}
