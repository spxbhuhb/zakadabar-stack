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
import kotlinx.serialization.KSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.authorize.LoginTimeout
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.ktor.buildServer
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.account.AccountPrivateBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.builtin.account.PrincipalBo
import zakadabar.stack.data.builtin.settings.ServerSettingsBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.PublicApi
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf

fun main(argv: Array<String>) {
    server = Server()
    server.main(argv)
}

val moduleLogger = LoggerFactory.getLogger("modules") !! // log module events

val routingLogger: Logger by lazy { LoggerFactory.getLogger("routing") } // trace routing events

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
inline fun <reified T : Any> module(noinline selector : (T) -> Boolean = { true }) = server.ModuleDependencyProvider(T::class, selector)

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
         *
         * For example, check AccountPrivateBackend in the demo.
         *
         * @return the public account bo and the id of the principal that belongs to this account
         */
        lateinit var findAccountById: (accountId: EntityId<AccountPrivateBo>) -> Pair<AccountPublicBo, EntityId<PrincipalBo>>

        /**
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

    private val test
            by option("--test", "-t").flag()


    private lateinit var settings: ServerSettingsBo

    private val modules = mutableListOf<BackendModule>()

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

        Sql.onCreate(settings.database) // initializes SQL connection

        loadModules(settings) // load modules

        Sql.onStart() // create missing tables and columns

        startModules() // start the modules

        ktorServer = buildServer(settings, modules) //  build the Ktor server instance

        staticRoot = settings.staticResources

        ktorServer.start(wait = ! test)
    }

    /**
     * Configuration function for the server. Calling this function is the very
     * first step of [run]. Default implementation is empty.
     */
    open fun onConfigure() {

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
                this += module

            } catch (ex: Throwable) {
                moduleLogger.error("failed to load module $it")
                throw ex
            }

        }
    }

    private fun startModules() {

        var success = true
        dependencies.forEach {
            success = success && it.resolve()
        }

        if (!success) throw IllegalArgumentException("module dependency resolution failed")

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
    inline fun <reified T : Any> first() = first(T::class)

    /**
     * Find a module of the given class. The class may be an interface.
     *
     * @param    kClass      The class to look for
     *
     * @return   First instance of [kClass] from the server modules.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    fun <T : Any> first(kClass: KClass<T>): T {
        @Suppress("UNCHECKED_CAST") // checking for class
        return modules.first { kClass.isInstance(it) } as T
    }

    /**
     * Find a module of the given class with a selector method called
     * to decided if the module is desired. The class may be an interface.
     *
     * @param    kClass      The class to look for
     * @param    selector    Function to select the instance.
     *
     * @return   First instance of [kClass] from the server modules.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    fun <T : Any> first(kClass: KClass<T>, selector : (T) -> Boolean): T {
        @Suppress("UNCHECKED_CAST") // checking for class
        return modules.first { kClass.isInstance(it) && selector(it as T) } as T
    }

    /**
     * Find a module of the given class. The class may be an interface.
     *
     * @return   First instance of [T] from the server modules or null if
     *           no such module exists.
     */
    @PublicApi
    inline fun <reified T : Any> firstOrNull() = firstOrNull(T::class)

    /**
     * Find a module of the given class. The class may be an interface.
     *
     * @param    kClass      The class to look for
     *
     * @return   First instance of [kClass] from the server modules or null if
     *           no such module exists.
     */
    fun <T : Any> firstOrNull(kClass: KClass<T>, selector : (T) -> Boolean = { true }): T? {
        @Suppress("UNCHECKED_CAST") // checking for class
        return modules.firstOrNull { kClass.isInstance(it) && selector(it as T) } as? T
    }

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
        private val dependentModule: Any?,
        private val dependentProperty: KProperty<*>,
        private val moduleClass: KClass<T>,
        private val selector: (T) -> Boolean
    ) {
        private var module: T? = firstOrNull(moduleClass, selector)

        init {
            dependencies += this
        }

        val name = dependentModule?.let { it::class.qualifiedName + "." } ?: ""

        fun resolve() =
            try {
                module = first(moduleClass, selector)
                moduleLogger.info("resolved dependency from ${name}${dependentProperty.name} to ${moduleClass.simpleName} ")
                true
            } catch (ex : NoSuchElementException) {
                moduleLogger.error("unable to resolve dependency from ${name}${dependentProperty.name} to ${moduleClass.simpleName} ")
                false
            }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return module !!
        }

    }

}