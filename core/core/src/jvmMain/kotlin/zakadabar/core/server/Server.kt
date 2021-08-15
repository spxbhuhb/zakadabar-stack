/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.server.netty.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import zakadabar.core.authorize.LoginTimeout
import zakadabar.core.persistence.exposed.Sql
import zakadabar.core.server.ktor.KtorServerBuilder
import zakadabar.core.setting.SettingBl
import zakadabar.core.log.Slf4jLogger
import zakadabar.core.module.CommonModule
import zakadabar.core.module.modules
import zakadabar.core.setting.SettingProvider
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.reflect.full.isSubclassOf

fun main(argv: Array<String>) {
    val properties = Properties()

    val stream = Server::class.java.getResourceAsStream("/zkBuild.properties")
    if (stream != null) {
        properties.load(stream)
        stream.close()
    }

    val version: String = properties.getProperty("version") ?: "unknown"
    val stackVersion: String = properties.getProperty("stackVersion") ?: "unknown"
    val projectName: String = properties.getProperty("projectName") ?: "unknown"

    modules.logger = Slf4jLogger("modules") // replace the stdout logger with LOGBack

    serverLogger.info("working directory: ${System.getProperty("user.dir")}")
    serverLogger.info("projectName=$projectName version=$version stackVersion=$stackVersion")

    server = Server(version)
    server.main(argv)
}

val serverLogger: Logger by lazy { LoggerFactory.getLogger("server") } // trace server events
val routingLogger: Logger by lazy { LoggerFactory.getLogger("routing") } // trace routing events
val settingsLogger = LoggerFactory.getLogger("settings") !! // log settings loads events

/**
 * The global server instance.
 */
lateinit var server: Server

enum class StartPhases(
    val optionName: String
) {
    SettingsLoad("settings-load"),
    ConnectDb("connect-db"),
    ModuleLoad("module-load"),
    InitializeDb("initialize-db"),
    ModuleStart("module-start"),
    Complete("complete")
}

open class Server(
    val version: String
) : CliktCommand() {

    companion object {
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

    }

    private val settingsPath
            by option("-s", "--settings", help = "Path to the settings file.")
                .file(mustExist = true, mustBeReadable = true, canBeDir = false)
                .convert { it.path }
                .default("./stack.server.yaml")

    private val startUntil
            by option("--start-until", help = "Start up until the given state (inclusive).")
                .choice(*StartPhases.values().map { it.optionName }.toTypedArray())
                .default(StartPhases.Complete.optionName)

    private val ignoreEnvironment
            by option("--ignore-environment", help = "Do not use environment variables when loading settings.")
                .flag(default = false)

    private val test
            by option("--test", "-t").flag()

    /**
     * The directory where setting files are. Set automatically by the configuration loader.
     */
    lateinit var settingsDirectory: Path

    lateinit var settings: ServerSettingsBo

    lateinit var description: ServerDescriptionBo

    val modules
        get() = zakadabar.core.module.modules

    lateinit var ktorServer: NettyApplicationEngine

    operator fun plusAssign(module: CommonModule) {
        this.modules += module
    }

    override fun run() {
        onConfigure()

        loadSettings()

        description = ServerDescriptionBo(
            name = settings.serverName,
            version = version,
            defaultLocale = settings.defaultLocale,
        )

        if (startUntil == StartPhases.SettingsLoad.optionName) return

        Sql.onCreate(settings.database) // initializes SQL connection

        settings.database.password = "" // don't keep DB password in the config

        if (startUntil == StartPhases.ConnectDb.optionName) return

        loadModules(settings)

        if (firstOrNull<SettingProvider>() == null) modules += SettingBl(!ignoreEnvironment, settingsDirectory)

        if (firstOrNull<ServerDescriptionBl>() == null) modules += ServerDescriptionBl()

        if (startUntil == StartPhases.ModuleLoad.optionName) return

        modules.resolveDependencies()

        initializeDb()

        if (startUntil == StartPhases.InitializeDb.optionName) return

        startModules()

        if (startUntil == StartPhases.ModuleStart.optionName) return

        ktorServer = onBuildServer()

        staticRoot = settings.staticResources

        ktorServer.start(wait = ! test)
    }

    /**
     * Configuration function for the server. Calling this function is the very
     * first step of [run]. Default implementation is empty.
     */
    open fun onConfigure() {

    }

    open fun loadSettings() {
        settings = ServerSettingLoader(!ignoreEnvironment).load(settingsPath)
        settingsDirectory = Paths.get(settings.settingsDirectory)
    }

    private fun loadModules(config: ServerSettingsBo) {

        config.modules.forEach {

            val installable = Server::class.java.classLoader.loadClass(it).kotlin

            require(installable.isSubclassOf(CommonModule::class)) { "module $it is not instance of CommonModule (maybe the name is wrong)" }

            modules += installable.objectInstance as CommonModule
        }

    }

    open fun initializeDb() {
        Sql.onStart() // create missing tables and columns
        modules.initializeDb()
    }

    open fun startModules() {
        modules.start()
    }

    open fun onBuildServer() = KtorServerBuilder(settings, modules).build() //  build the Ktor server instance

    fun shutdown(gracePeriodMillis : Long = 0, timeoutMillis : Long = 2000) {
        server.ktorServer.stop(gracePeriodMillis, timeoutMillis)
        modules.stop()
        modules.clear()
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
    open fun onLoginTimeout(call: ApplicationCall) {
        if (call.request.uri.startsWith("/api")) {
            throw LoginTimeout()
        }
    }

    /**
     * Adds the value of the apiCacheControl setting to the response headers.
     * This is "no-cache, no-store" by default. To change it, modify the
     * setting in the configuration file or override this method.
     */
    open fun apiCacheControl(call: ApplicationCall) {
        call.response.headers.append(HttpHeaders.CacheControl, settings.apiCacheControl)
    }

    /**
     * Find a module of the given class. The class may be an interface.
     *
     * @return   First instance of [T] from the server modules.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    inline fun <reified T : Any> first() = modules.first(T::class)


    /**
     * Find a module of the given class. The class may be an interface.
     *
     * @return   First instance of [T] from the server modules or null if
     *           no such module exists.
     */
    inline fun <reified T : Any> firstOrNull() = modules.firstOrNull(T::class)

}