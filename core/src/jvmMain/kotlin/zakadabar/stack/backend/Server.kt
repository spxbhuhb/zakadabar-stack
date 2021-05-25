/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import kotlinx.serialization.KSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.builtin.session.LoginTimeout
import zakadabar.stack.backend.data.builtin.session.SessionBackend
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.ktor.buildServer
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.account.AccountPrivateBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.builtin.account.PrincipalBo
import zakadabar.stack.data.builtin.settings.ServerSettingsBo
import zakadabar.stack.data.entity.EntityId
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.full.isSubclassOf

val moduleLogger = LoggerFactory.getLogger("modules") !! // log module events

val routingLogger: Logger by lazy { LoggerFactory.getLogger("routing") } // trace routing events

lateinit var server : Server

fun main(argv: Array<String>) {
    server = Server()
    server.main(argv)
}

open class Server : CliktCommand() {

    companion object {
        /**
         * This variable contains the anonymous account. Each server should have one
         * this is used for public access.
         *
         * For example, check AccountPrivateBackend in the demo.
         */
        lateinit var anonymous: AccountPublicBo

        /**
         * Find an account by its id. Used by [SessionBackend].
         *
         * For example, check AccountPrivateBackend in the demo.
         *
         * @return the public account bo and the id of the principal that belongs to this account
         */
        lateinit var findAccountById: (accountId: EntityId<AccountPrivateBo>) -> Pair<AccountPublicBo, EntityId<PrincipalBo>>

        /**
         * Find an account by its id. Used by [SessionBackend].
         *
         * For example, check AccountPrivateBackend.
         *
         * @return the public account bo and the id of the principal that belongs to this account
         */
        lateinit var findAccountByName: (accountName: String) -> Pair<AccountPublicBo, EntityId<PrincipalBo>>

        /**
         * The directory where setting files are. Set automatically by the configuration loader.
         */
        lateinit var settingsDirectory: Path

        /**
         * When true GET (read and query) requests are logged by bo backends.
         */
        var logReads: Boolean = true

        /**
         * When true, POST and PATCH handled by EntityBackends and ActionBackends validate
         * incoming bo objects. When invalid the request returns with 400 Bad Request.
         * Default is true, may be switched off by backend, using the validate property
         * of the backend.
         */
        var validate = true

        /**
         * When true the server is shutting down and background tasks should stop.
         * TODO replace shutdown flag with an event driven system
         */
        var shutdown: Boolean = false
            get() = synchronized(field) { field }
            set(value) {
                synchronized(field) { field = value }
            }

        lateinit var staticRoot: String

        private val modules = mutableListOf<BackendModule>()

        private val entityBackends = mutableListOf<EntityBackend<*>>()

        private val customBackends = mutableListOf<CustomBackend>()

        operator fun plusAssign(boBackend: EntityBackend<*>) {
            this.modules += boBackend
            this.entityBackends += boBackend
            boBackend.onModuleLoad()
        }

        operator fun plusAssign(customBackend: CustomBackend) {
            this.modules += customBackend
            this.customBackends += customBackend
            customBackend.onModuleLoad()
        }

        fun <T : BaseBo> loadSettings(namespace: String, serializer: KSerializer<T>): T? {

            val p1 = settingsDirectory.resolve("$namespace.yaml")
            val p2 = settingsDirectory.resolve("$namespace.yml")

            val source = when {
                Files.isReadable(p1) -> Files.readAllBytes(p1).decodeToString()
                Files.isReadable(p2) -> Files.readAllBytes(p2).decodeToString()
                else -> null
            } ?: return null

            return Yaml.default.decodeFromString(serializer, source)
        }
    }

    private val settingsPath
        by option("-s", "--settings", help = "Path to the settings file.")
            .file(mustExist = true, mustBeReadable = true, canBeDir = false)
            .convert { it.path }
            .default("./zakadabar.stack.server.yaml")

    private lateinit var settings : ServerSettingsBo

    override fun run() {

        settings = loadServerSettings()

        Sql.onCreate(settings.database) // initializes SQL connection

        loadModules(settings) // load modules

        Sql.onStart() // create missing tables and columns

        startModules() // start the modules

        val server = buildServer(settings, entityBackends, customBackends) //  build the Ktor server instance

        staticRoot = settings.staticResources

        server.start(wait = true)
    }

    private fun loadServerSettings(): ServerSettingsBo {

        val paths = listOf(
            settingsPath,
            "./zakadabar.stack.server.yml",
            "./etc/zakadabar.stack.server.yaml",
            "./etc/zakadabar.stack.server.yml",
            "../etc/zakadabar.stack.server.yaml",
            "../etc/zakadabar.stack.server.yml",
            "./template/app/etc/zakadabar.stack.server.yaml" // this is for development, TODO remove hard-coded development config path
        )

        for (p in paths) {
            val path = Paths.get(p)
            if (! Files.exists(path)) continue

            settingsDirectory = path.parent
            val source = Files.readAllBytes(path).decodeToString()

            return Yaml.default.decodeFromString(ServerSettingsBo.serializer(), source)
        }

        throw IllegalArgumentException("cannot locate server settings file")
    }

    private fun loadModules(config: ServerSettingsBo) {

        config.modules.forEach {

            val installable = Server::class.java.classLoader.loadClass(it).kotlin

            require(installable.isSubclassOf(BackendModule::class)) { "module $it is not instance of BackendModule (maybe the name is wrong)" }

            try {

                val module = (installable.objectInstance as BackendModule)
                modules += module

                when (module) {
                    is EntityBackend<*> -> entityBackends += module
                    is CustomBackend -> customBackends += module
                    else -> {
                        modules += module
                        module.onModuleLoad()
                    }
                }

                moduleLogger.info("loaded module $it")

            } catch (ex: Throwable) {
                moduleLogger.error("failed to load module $it")
                throw ex
            }

        }
    }

    private fun startModules() {
        modules.forEach {
            try {
                it.onModuleStart()
                moduleLogger.info("started module $it")
            } catch (ex: Throwable) {
                moduleLogger.error("failed to start module $it")
                throw ex
            }
        }
    }

    /**
     * Called when the request has a session cookie which is not known by the
     * server. This typically means that the session expired on the server
     * side but the client still remembers the cookie.
     *
     * When this is an API call we can respond with 440 Login Timeout because
     * the client side application is already running and we can assume that
     * it will handle 440 properly.
     *
     * When this is not an API we should let it go through, so the client can
     * access index.html and other resources to start the application. In this
     * case the application starts from the beginning, so it will ask the client
     * to login if needed.
     *
     * You can override this method to switch off the login timeout mechanics
     * or to add other paths to it.
     */
    open fun onLoginTimeout(call : ApplicationCall) {
        if (call.request.uri.startsWith("/api")) {
            throw LoginTimeout()
        }
    }

    /**
     * Adds the value of the apiCacheControl setting to the response headers.
     * This is "no-cache, no-store" by default. To change it, modify the
     * setting in the configuration file or override this method.
     */
    open fun apiCacheControl(call : ApplicationCall) {
        call.response.headers.append(HttpHeaders.CacheControl, settings.apiCacheControl)
    }
}
