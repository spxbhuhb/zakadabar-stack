/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.app

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
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import zakadabar.stack.Stack
import zakadabar.stack.backend.BackendContext
import zakadabar.stack.backend.builtin.account.data.AccountTable
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.builtin.entities.data.Locks
import zakadabar.stack.backend.builtin.entities.data.SnapshotTable
import zakadabar.stack.backend.extend.BackendModule
import zakadabar.stack.backend.session.StackSession
import zakadabar.stack.backend.session.session
import zakadabar.stack.backend.util.Sql
import zakadabar.stack.backend.util.executor
import zakadabar.stack.backend.ws.StackServerSession
import zakadabar.stack.data.FolderDto
import zakadabar.stack.data.SystemDto
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.util.Executor
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import kotlin.reflect.full.isSubclassOf
import zakadabar.stack.backend.builtin.account.Backend as AccountBackend

val wsLogger = LoggerFactory.getLogger("ws") !! // log ws events

val moduleLogger = LoggerFactory.getLogger("modules") !! // log module events

fun main(argv: Array<String>) = Server().main(argv)

class Server : CliktCommand() {

    private val configPath
            by option("-c", "--config", help = "Path to the configuration file or directory.")
                .file(mustExist = true, mustBeReadable = true, canBeDir = true)
                .convert { it.path }
                .default("./zakadabar-config.yaml")

    override fun run() {

        val config = loadConfig()

        dbBootstrap(config)

        loadModules(config)

        val server = embeddedServer(Netty, port = config.ktor.port) {

            install(Sessions) {
                cookie<StackSession>("StackSessionId", SessionStorageMemory()) { // TODO replace this with SQL storage
                    cookie.path = "/"
                }
            }

            install(Authentication) {
                session()

                basic(name = "basic") {
                    realm = config.serverName
                    validate { credentials -> return@validate Executor(BackendContext.anonymous.id.value) }
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

            routing {
                if (config.traceRouting) trace { wsLogger.trace(it.buildText()) }

                route("auth") {
                    authenticate("basic") {
                        route("basic") {
                            get("login") {
                                // FIXME not system :D
                                call.sessions.set(StackSession(BackendContext.system.id.value))
                                call.respondText(BackendContext.system.id.value.toString(), ContentType.Application.Json)
                            }
                            get("logout") {
                                call.sessions.set(StackSession(BackendContext.anonymous.id.value))
                                call.respondText(BackendContext.anonymous.id.value.toString(), ContentType.Application.Json)
                            }
                        }
                    }
                }

                authenticate {

                    route("api") {

                        get("${Stack.shid}/health") {
                            call.respondText("OK", ContentType.Text.Plain)
                        }

                        get("${Stack.shid}/who-am-i") {
                            call.respondText(call.executor().entityId.toString(), ContentType.Application.Json)
                        }

                        webSocket("${Stack.shid}/ws") {
                            wsLogger.info("incoming ws connection on ${Stack.shid}")
                            StackServerSession(this).runReceive()
                        }

                        // api installs add routes and the code to serve them
                        BackendContext.modules.forEach {
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

    private fun dbBootstrap(config: Configuration) {

        Sql.init(config.database)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                EntityTable,
                SnapshotTable,
                Locks,
                zakadabar.stack.backend.builtin.entities.data.Sessions,
                AccountTable // FIXME make this swappable
            )
        }

        transaction {
            val systemDao = EntityDao.find { EntityTable.type eq SystemDto.type }.firstOrNull()

            if (systemDao != null) return@transaction

            val now = Clock.System.now().toJavaInstant()

            // this is a temporary entity for bootstrap

            EntityTable.insert {
                it[id] = EntityID(0L, EntityTable)
                it[acl] = EntityID(0L, EntityTable)
                it[status] = EntityStatus.Active
                it[parent] = null
                it[name] = "bootstrap"
                it[type] = "bootstrap"
                it[size] = 0
                it[revision] = 1
                it[createdAt] = now
                it[createdBy] = EntityID(0L, EntityTable)
                it[modifiedAt] = now
                it[modifiedBy] = EntityID(0L, EntityTable)
            }

            val system = EntityDao.new {
                name = "System"
                acl = null
                status = EntityStatus.Active
                parent = null
                type = SystemDto.type
                size = 0
                revision = 1
                createdAt = now
                createdBy = EntityDao[0L]
                modifiedAt = now
                modifiedBy = EntityDao[0L]
            }

            commit()

            system.createdBy = system
            system.modifiedBy = system

            commit()

            EntityTable.deleteWhere { EntityTable.id eq 0L }

            val pid = Executor(system.id.value)

            val accountsDao = EntityDao.create("accounts", FolderDto.type, 1L, EntityStatus.Active, pid)
            EntityDao.create("roles", FolderDto.type, 1L, EntityStatus.Active, pid)
            EntityDao.create("tmp", FolderDto.type, 1L, EntityStatus.Active, pid)
            EntityDao.create("bin", FolderDto.type, 1L, EntityStatus.Active, pid)

            val soDto = CommonAccountDto(
                id = 0,
                entityRecord = EntityRecordDto.new(accountsDao.id.value, CommonAccountDto.type, "so"),
                emailAddress = "noreply@simplexion.hu",
                fullName = "Security Officer",
                displayName = "Security Officer",
                organizationName = "Simplexion Kft.",
                avatar = null,
                password = "df;kgh32yfsdhfoia"
            )

            AccountBackend.create(pid, soDto)

            val anonymousDto = CommonAccountDto(
                id = 0,
                entityRecord = EntityRecordDto.new(accountsDao.id.value, CommonAccountDto.type, "anonymous"),
                emailAddress = "noreply@simplexion.hu",
                fullName = "Anonymous",
                displayName = "Anonymous",
                organizationName = "Simplexion Kft.",
                avatar = null,
                password = "df;kgh32yfsdhfoia"
            )

            AccountBackend.create(pid, anonymousDto)

        }

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
        // load extensions

        config.modules.forEach {

            val installable = Server::class.java.classLoader.loadClass(it).kotlin

            require(installable.isSubclassOf(BackendModule::class)) { "module $it is not loadable (maybe the name is wrong)" }

            try {

                val module = (installable.objectInstance as BackendModule)
                BackendContext += module
                module.onLoad()

                moduleLogger.info("loaded module $it")

            } catch (ex: Throwable) {
                moduleLogger.error("failed to load module $it")
                throw ex
            }

        }

        // backend installs create sql tables and whatever else they need

        BackendContext.modules.forEach {
            it.init()
            moduleLogger.info("initialized module $it")
        }
    }

}
