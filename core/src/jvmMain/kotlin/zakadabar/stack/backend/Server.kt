/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.server.netty.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.authorize.LoginTimeout
import zakadabar.stack.backend.builtin.ServerDescriptionBl
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.ktor.KtorServerBuilder
import zakadabar.stack.backend.setting.SettingBl
import zakadabar.stack.backend.setting.SettingProvider
import zakadabar.stack.data.builtin.misc.ServerDescriptionBo
import zakadabar.stack.data.builtin.settings.ServerSettingsBo
import zakadabar.stack.util.InstanceStore
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
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

    moduleLogger.info("server projectName=$projectName version=$version stackVersion=$stackVersion")

    server = Server(version)
    server.main(argv)
}

val moduleLogger = LoggerFactory.getLogger("modules") !! // log module events
val routingLogger: Logger by lazy { LoggerFactory.getLogger("routing") } // trace routing events
val settingsLogger = LoggerFactory.getLogger("settings") !! // log settings loads events

/**
 * The global server instance.
 */
lateinit var server: Server

/**
 * Provides a delegate that is a reference to a backend module. The dependency
 * is resolved after all modules are loaded and before any modules are started.
 *
 * When the dependency cannot be resolved:
 *
 * - reports an error on the console,
 * - aborts server startup.
 *
 * @param  selector  Function to select between modules if there are more than one.
 *                   Default selects the first.
 */
inline fun <reified T : Any> module(noinline selector: (T) -> Boolean = { true }) = server.ModuleDependencyProvider(T::class, selector)

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
                .default("./zakadabar.stack.server.yaml")

    private val test
            by option("--test", "-t").flag()

    /**
     * The directory where setting files are. Set automatically by the configuration loader.
     */
    lateinit var settingsDirectory: Path

    lateinit var settings: ServerSettingsBo

    lateinit var description: ServerDescriptionBo

    val modules = InstanceStore<BackendModule>()

    private val dependencies = mutableListOf<ModuleDependency<*>>()

    lateinit var ktorServer: NettyApplicationEngine

    operator fun plusAssign(module: BackendModule) {
        this.modules += module
        module.onModuleLoad()
        moduleLogger.info("loaded module $module")
    }

    override fun run() {

        onConfigure()

        settings = loadServerSettings()

        description = ServerDescriptionBo(
            name = settings.serverName,
            version = version,
            defaultLocale = settings.defaultLocale,
        )

        Sql.onCreate(settings.database) // initializes SQL connection

        settings.database.password = "" // don't keep DB password in the config

        loadModules(settings)

        if (firstOrNull<SettingProvider>() == null) this += SettingBl()

        if (firstOrNull<ServerDescriptionBl>() == null) this += ServerDescriptionBl()

        resolveDependencies()

        initializeDb()

        startModules()

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

    open fun onBuildServer() = KtorServerBuilder(settings, modules).build() //  build the Ktor server instance

    private fun loadServerSettings(): ServerSettingsBo {

        val paths = listOf(
            settingsPath,
            "./stack.server.yml",
            "./etc/stack.server.yaml",
            "./etc/stack.server.yml",
            "../etc/stack.server.yaml",
            "../etc/stack.server.yml",
            "./template/app/etc/stack.server.yaml" // this is for development, TODO remove hard-coded development config path
        )

        for (p in paths) {
            val path = Paths.get(p)
            if (! Files.exists(path)) continue

            settingsDirectory = path.parent
            val source = Files.readAllBytes(path).decodeToString()

            settingsLogger.info("ServerSettingsBo source: ${path.toAbsolutePath()}")

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
                this += module

            } catch (ex: Throwable) {
                moduleLogger.error("failed to load module $it")
                throw ex
            }

        }
    }

    private fun resolveDependencies() {
        var success = true
        dependencies.forEach {
            success = success && it.resolve()
        }

        if (! success) throw IllegalArgumentException("module dependency resolution failed")
    }

    private fun initializeDb() {
        Sql.onStart() // create missing tables and columns

        modules.instances.forEach {
            try {
                it.onInitializeDb()
                moduleLogger.debug("initialized DB for module $it")
            } catch (ex: Throwable) {
                moduleLogger.error("failed to initialize DB for module $it")
                throw ex
            }
        }
    }

    private fun startModules() {
        modules.instances.forEach {
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

    inner class ModuleDependencyProvider<T : Any>(
        private val moduleClass: KClass<T>,
        private val selector: (T) -> Boolean
    ) {
        operator fun provideDelegate(thisRef: Any, property: KProperty<*>) =
            ModuleDependency(thisRef, property, moduleClass, selector)

        operator fun provideDelegate(thisRef: Nothing?, property: KProperty<*>) =
            ModuleDependency(thisRef, property, moduleClass, selector)
    }

    inner class ModuleDependency<T : Any>(
        dependentModule: Any?,
        private val dependentProperty: KProperty<*>,
        private val moduleClass: KClass<T>,
        private val selector: (T) -> Boolean
    ) {
        private var module: T? = modules.firstOrNull(moduleClass, selector)

        init {
            dependencies += this
        }

        val name = dependentModule?.let { it::class.qualifiedName + "." } ?: ""

        fun resolve() =
            try {
                module = modules.first(moduleClass, selector)
                moduleLogger.info("resolved dependency from ${name}${dependentProperty.name} to ${moduleClass.simpleName} ")
                true
            } catch (ex: NoSuchElementException) {
                moduleLogger.error("unable to resolve dependency from ${name}${dependentProperty.name} to ${moduleClass.simpleName} ")
                false
            }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return module !!
        }

    }

}