/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

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
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import zakadabar.core.authorize.LoginTimeout
import zakadabar.core.exception.*
import zakadabar.core.module.ModuleStore
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.Server.Companion.staticRoot
import zakadabar.core.server.ServerSettingsBo
import zakadabar.core.server.routingLogger
import zakadabar.core.server.server
import java.io.File
import java.time.Duration

/**
 * Build a Ktor server instance.
 */
open class KtorServerBuilder(
    open val config: ServerSettingsBo,
    open val modules: ModuleStore,
) {

    fun build() : ApplicationEngine = embeddedServer(
        factory = Class.forName(config.ktor.engine).kotlin.objectInstance as ApplicationEngineFactory<*, *>,
        port = config.ktor.port
    ) {

        features.forEach {
            @Suppress("UNCHECKED_CAST")
            install(it.feature, it.config as (Any.() -> Unit))
        }

        install(ContentNegotiation) {
            json(Json)
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
                configBuilders.forEach { if (it is KtorAuthConfig) it.runBuild(this) }
            }
        } else {
            install(Authentication) {
                var configured = false
                configBuilders.forEach {
                    if (it is KtorAuthConfig) {
                        it.runBuild(this)
                        configured = true
                    }
                }
                if (!configured) configureEmpty()
            }
        }
    }

    open fun Application.statusPages() {
        install(StatusPages) {
            exception<LoginTimeout> {
                call.respond(HttpStatusCode(440, "Login Timeout"))
            }
            exception<EntityNotFoundException> {
                call.respond(HttpStatusCode.NotFound)
            }
            exception<Forbidden> {
                if (call.principal<KtorExecutor>()?.anonymous != false) {
                    call.respond(HttpStatusCode.Unauthorized, UnauthorizedData())
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                }
            }
            exception<Unauthorized> {
                call.respond(HttpStatusCode.Unauthorized, it.data)
            }
            exception<DataConflict> {
                call.respond(HttpStatusCode.Conflict, it.message)
            }
            exception<BadRequest> {
                call.respond(HttpStatusCode.BadRequest, it.validityReport)
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

            configBuilders.forEach {
                if (it is KtorRouteConfig) it.runBuild(this)
            }

            authenticate {
                install(config, modules)
            }

        }
    }

    open fun Route.install(config: ServerSettingsBo, modules: ModuleStore) {

        get("health") {
            call.respondText("OK", ContentType.Text.Plain)
        }

        route("api") {

            modules.forEach {
                if (it is RoutedModule) {
                    it.onInstallRoutes(this)
                }
            }
            get("health") {
                call.respondText("OK", ContentType.Text.Plain)
            }

        }

        static {
            staticRootFolder = File(config.staticResources)
            files(".")
            default("index.html")

            modules.forEach {
                if (it is RoutedModule) {
                    it.onInstallStatic(this)
                }
            }

        }
    }

}
