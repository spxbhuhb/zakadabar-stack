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
import kotlinx.serialization.KSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.Sql
import zakadabar.stack.backend.data.builtin.session.SessionBackend
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.backend.ktor.buildServer
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.account.AccountPrivateDto
import zakadabar.stack.data.builtin.account.AccountPublicDto
import zakadabar.stack.data.builtin.account.PrincipalDto
import zakadabar.stack.data.builtin.settings.ServerSettingsDto
import zakadabar.stack.data.record.RecordId
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.full.isSubclassOf

val moduleLogger = LoggerFactory.getLogger("modules") !! // log module events

val routingLogger: Logger by lazy { LoggerFactory.getLogger("routing") } // trace routing events

fun main(argv: Array<String>) = Server().main(argv)

class Server : CliktCommand() {

    companion object {
        /**
         * This variable contains the anonymous account. Each server should have one
         * this is used for public access.
         *
         * For example, check AccountPrivateBackend in the demo.
         */
        lateinit var anonymous: AccountPublicDto

        /**
         * Find an account by its id. Used by [SessionBackend].
         *
         * For example, check AccountPrivateBackend in the demo.
         *
         * @return the public account dto and the id of the principal that belongs to this account
         */
        lateinit var findAccountById: (accountId: RecordId<AccountPrivateDto>) -> Pair<AccountPublicDto, RecordId<PrincipalDto>>

        /**
         * Find an account by its id. Used by [SessionBackend].
         *
         * For example, check AccountPrivateBackend in the demo.
         *
         * @return the public account dto and the id of the principal that belongs to this account
         */
        lateinit var findAccountByName: (accountName: String) -> Pair<AccountPublicDto, RecordId<PrincipalDto>>

        /**
         * The directory where setting files are. Set automatically by the configuration loader.
         */
        lateinit var settingsDirectory: Path

        /**
         * When true GET (read and query) requests are logged by DTO backends.
         */
        var logReads: Boolean = true

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

        private val dtoBackends = mutableListOf<RecordBackend<*>>()

        private val customBackends = mutableListOf<CustomBackend>()

        operator fun plusAssign(dtoBackend: RecordBackend<*>) {
            this.modules += dtoBackend
            this.dtoBackends += dtoBackend
            dtoBackend.onModuleLoad()
        }

        operator fun plusAssign(customBackend: CustomBackend) {
            this.modules += customBackend
            this.customBackends += customBackend
            customBackend.onModuleLoad()
        }

        fun <T : DtoBase> loadSettings(namespace: String, serializer: KSerializer<T>): T? {

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

    override fun run() {

        val config = loadServerSettings()

        Sql.onCreate(config.database) // initializes SQL connection

        loadModules(config) // load modules

        Sql.onStart() // create missing tables and columns

        startModules() // start the modules

        val server = buildServer(config, dtoBackends, customBackends) //  build the Ktor server instance

        staticRoot = config.staticResources

        server.start(wait = true)
    }

    private fun loadServerSettings(): ServerSettingsDto {

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

            return Yaml.default.decodeFromString(ServerSettingsDto.serializer(), source)
        }

        throw IllegalArgumentException("cannot locate server settings file")
    }

    private fun loadModules(config: ServerSettingsDto) {

        config.modules.forEach {

            val installable = Server::class.java.classLoader.loadClass(it).kotlin

            require(installable.isSubclassOf(BackendModule::class)) { "module $it is not instance of BackendModule (maybe the name is wrong)" }

            try {

                val module = (installable.objectInstance as BackendModule)
                modules += module

                when (module) {
                    is RecordBackend<*> -> dtoBackends += module
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

}
