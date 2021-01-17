/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import org.slf4j.LoggerFactory
import zakadabar.stack.Stack
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.Sql
import zakadabar.stack.backend.data.builtin.session.SessionStorageSql
import zakadabar.stack.backend.data.builtin.session.StackSession
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.AccountPublic
import zakadabar.stack.util.Executor
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import kotlin.reflect.full.isSubclassOf

val moduleLogger = LoggerFactory.getLogger("modules") !! // log module events

fun main(argv: Array<String>) = Server().main(argv)

class Server : CliktCommand() {

    companion object {
        lateinit var anonymous: AccountPublic

        /**
         * When true GET (read and query) requests are logged by DTO backends.
         */
        var logReads: Boolean = true

        private val dtoBackends = mutableListOf<RecordBackend<*>>()

        private val customBackends = mutableListOf<CustomBackend>()

        operator fun plusAssign(dtoBackend: RecordBackend<*>) {
            this.dtoBackends += dtoBackend
            dtoBackend.init()
        }

        operator fun plusAssign(customBackend: CustomBackend) {
            this.customBackends += customBackend
            customBackend.init()
        }
    }

    private val configPath
            by option("-c", "--config", help = "Path to the configuration file or directory.")
                .file(mustExist = true, mustBeReadable = true, canBeDir = true)
                .convert { it.path }
                .default("./zakadabar-config.yaml")

    override fun run() {

        val config = loadConfig()

        Sql.init(config.database)

        loadModules(config)

        val server = embeddedServer(Netty, port = config.ktor.port) {

            install(Sessions) {
                cookie<StackSession>("STACK_SESSION", SessionStorageSql) {
                    cookie.path = "/"
                }
            }

            install(Authentication) {
                session()

                basic(name = "basic") {
                    realm = config.serverName
                    validate { return@validate Executor(0L) }
                }

            }

            install(ContentNegotiation) {
                json()
            }

            install(WebSockets) {
                val c = config.ktor.websocket
                pingPeriod = Duration.ofSeconds(c.pingPeriod)
                timeout = Duration.ofSeconds(c.timeout)
                maxFrameSize = c.maxFrameSize
                masking = c.masking
            }

            install(StatusPages) {
                statusFile(HttpStatusCode.NotFound, filePattern = "index.html")
            }

            routing {

                route("auth") {
                    authenticate("basic") {
                        route("basic") {
                            get("login") {
                                // FIXME not anonymous :D
                                call.sessions.set(StackSession(0))
                                call.respondText(0L.toString(), ContentType.Application.Json)
                            }
                            get("logout") {
                                call.sessions.set(StackSession(anonymous.id))
                                call.respondText(anonymous.id.toString(), ContentType.Application.Json)
                            }
                        }
                    }
                }

                authenticate {

                    route("api") {

                        get("${Stack.shid}/health") {
                            call.respondText("OK", ContentType.Text.Plain)
                        }

                        // api installs add routes and the code to serve them
                        dtoBackends.forEach {
                            it.install(this)
                        }

                        customBackends.forEach {
                            it.install(this)
                        }

                    }

                    // TODO move static stuff under an URL like "static/"
                    static {
                        staticRootFolder = File(config.staticResources)
                        files(".")
                        default("index.html")
                    }

                }

            }
        }

        server.start(wait = true)
    }

    private fun loadConfig(): Configuration {

        val paths = listOf(
            configPath,
            "./zakadabar-server.yml",
            "./etc/zakadabar-server.yaml",
            "./etc/zakadabar-server.yml",
            "../etc/zakadabar-server.yaml",
            "../etc/zakadabar-server.yml",
            "./app/etc/zakadabar-server.yaml" // this is for development
        )

        for (p in paths) {
            val path = Paths.get(p)
            if (! Files.exists(path)) continue

            val source = Files.readAllBytes(path).decodeToString()
            return Yaml.default.decodeFromString(Configuration.serializer(), source)
        }

        throw IllegalArgumentException("cannot locate configuration file")
    }

    private fun loadModules(config: Configuration) {

        val modules = mutableListOf<BackendModule>()

        config.modules.forEach {

            val installable = Server::class.java.classLoader.loadClass(it).kotlin

            require(installable.isSubclassOf(BackendModule::class)) { "module $it is not loadable (maybe the name is wrong)" }

            try {

                modules += (installable.objectInstance as BackendModule)
                moduleLogger.info("loaded module $it")

            } catch (ex: Throwable) {
                moduleLogger.error("failed to load module $it")
                throw ex
            }

        }

        // backend installs create sql tables and whatever else they need

        modules.forEach {
            when (it) {
                is RecordBackend<*> -> dtoBackends += it
                is CustomBackend -> customBackends += it
                else -> it.init()
            }
            moduleLogger.info("initialized module $it")
        }
    }

}
