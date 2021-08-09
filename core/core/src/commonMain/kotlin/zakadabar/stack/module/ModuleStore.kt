/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.module

import zakadabar.stack.log.Logger
import zakadabar.stack.log.StdOutLogger
import zakadabar.stack.module.ModuleStartupBucket.Companion.BOOTSTRAP
import zakadabar.stack.module.ModuleStartupBucket.Companion.DEFAULT
import zakadabar.stack.module.ModuleStartupBucket.Companion.PUBLISH
import zakadabar.stack.module.ModuleStartupBucket.Companion.STEADY
import zakadabar.stack.util.Lock
import zakadabar.stack.util.use
import kotlin.reflect.KClass

/**
 * Store and management of application modules.
 *
 * This class is thread safe.
 */
open class ModuleStore {

    open val lock = Lock()

    open var logger: Logger = StdOutLogger()

    protected val modules = mutableListOf<CommonModule>()

    protected val dependencies = mutableListOf<ModuleDependency<*>>()

    protected var defaultBucket: ModuleStartupBucket

    protected val buckets = mutableListOf(
        ModuleStartupBucket(BOOTSTRAP) { it is ModuleStartupBucket.Companion.BootstrapBucket },
        ModuleStartupBucket(DEFAULT) { true }.also { defaultBucket = it },
        ModuleStartupBucket(PUBLISH) { it is ModuleStartupBucket.Companion.PublishBucket },
        ModuleStartupBucket(STEADY) { it is ModuleStartupBucket.Companion.SteadyBucket }
    )

    /**
     * Add a module to this module store. Calls `onModuleLoad`.
     */
    open operator fun plusAssign(instance: CommonModule) {
        lock.use {
            try {
                modules += instance
                addToBucket(instance)
                instance.onModuleLoad()
                logger.info("loaded module $instance")
            } catch (ex: Throwable) {
                logger.error("failed to load module $instance", ex)
                throw ex
            }
        }
    }

    /**
     * Add a bucket to this module store.
     */
    open operator fun plusAssign(bucket: ModuleStartupBucket) {
        lock.use {
            buckets += bucket
            buckets.sortBy { it.order }
        }
    }

    /**
     * Add a dependency to this module store.
     */
    open operator fun plusAssign(dependency: ModuleDependency<*>) {
        lock.use {
            // ModuleDependency is an open class, do not do anything else here
            dependencies += dependency
        }
    }

    /**
     * Add the instance to the appropriate bucket. Add to default
     * if there is no better match.
     *
     * Runs under [lock].
     */
    protected fun addToBucket(instance: CommonModule) {

        buckets.forEach { bucket ->
            if (bucket.order == DEFAULT) return@forEach
            if (bucket.selector(instance)) {
                bucket += instance
                return@addToBucket
            }
        }

        defaultBucket += instance
    }

    /**
     * Call `onInitializeDb` for each bucket.
     */
    open fun initializeDb() {
        lock.use {
            buckets.forEach { it.initializeDb(this) }
        }
    }

    /**
     * Resolve all dependencies registered in "dependencies".
     */
    open fun resolveDependencies() {
        lock.use {
            var success = true

            dependencies.forEach {
                success = it.resolve() && success
            }

            if (! success) throw IllegalStateException("module dependency resolution failed")
        }
    }

    /**
     * Start all modules in the store.
     */
    open fun start() {
        lock.use {
            buckets.forEach { it.start(this) }
        }
    }

    /**
     * Call `onModuleStop` for all modules.
     */
    open fun stop() {
        lock.use {
            buckets.reversed().forEach { it.stop(this) }
        }
    }

    /**
     * Stop all modules and clear the content of the module store.
     */
    open fun clear() {
        lock.use {
            buckets.forEach { it.clear() }
            modules.clear()
            dependencies.clear()
        }
    }

    /**
     * Find a module of the given class. The class may be an interface.
     *
     * @param    T      The class to look for
     *
     * @return   First module of [T] from the module store.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    inline fun <reified T : Any> first() = first(T::class)

    /**
     * Find a module of the given class. The class may be an interface.
     *
     * @param    T           The class to look for
     * @param    selector    Function to select the module.
     *
     * @return   First module of [T] from the module store.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    inline fun <reified T : Any> first(noinline selector: (T) -> Boolean) = first(T::class, selector)

    /*
     * Find a module of the given class. The class may be an interface.
     *
     * @param    T      The class to look for
     *
     * @return   First module of [T] from the module store.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    open fun <T : Any> first(kClass: KClass<T>): T {
        lock.use {
            @Suppress("UNCHECKED_CAST") // checking for class
            return modules.first { kClass.isInstance(it) } as T
        }
    }

    /**
     * Find a instance of the given class with a selector method called
     * to decided if the instance is desired. The class may be an interface.
     *
     * @param    kClass      The class to look for
     * @param    selector    Function to select the module.
     *
     * @return   First instance of [kClass] from the module store.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    open fun <T : Any> first(kClass: KClass<T>, selector: (T) -> Boolean): T {
        @Suppress("UNCHECKED_CAST") // checking for class
        return modules.first { kClass.isInstance(it) && selector(it as T) } as T
    }

    /**
     * Find a instance of the given class, return with null when not found.
     * The class may be an interface.
     *
     * @param    T      The class to look for
     *
     * @return   First instance of [T] from the module store or null.
     */
    inline fun <reified T : Any> firstOrNull() = firstOrNull(T::class)

    /**
     * Find a instance of the given class, return with null when not found.
     * The class may be an interface.
     *
     * @param    T      The class to look for
     * @param    selector    Function to select the module.
     *
     * @return   First module of [T] from the module store or null.
     */
    inline fun <reified T : Any> firstOrNull(noinline selector: (T) -> Boolean) = firstOrNull(T::class, selector)

    /**
     * Find a instance of the given class, return with null when not found.
     * The class may be an interface.
     *
     * @param    kClass      The class to look for
     *
     * @return   First instance of [kClass] from the module store or null.
     */
    open fun <T : Any> firstOrNull(kClass: KClass<T>): T? {
        lock.use {
            @Suppress("UNCHECKED_CAST") // checking for class
            return modules.firstOrNull { kClass.isInstance(it) } as? T
        }
    }

    /**
     * Find a module of the given class with a selector method called
     * to decided if the module is desired. The class may be an interface.
     * Return with null when not found.
     *
     * @param    kClass      The class to look for
     * @param    selector    Function to select the module.
     *
     * @return   First instance of [kClass] from the the module store or null.
     */
    open fun <T : Any> firstOrNull(kClass: KClass<T>, selector: (T) -> Boolean): T? {
        lock.use {
            @Suppress("UNCHECKED_CAST") // checking for class
            return modules.firstOrNull { kClass.isInstance(it) && selector(it as T) } as? T
        }
    }

    /**
     * Execute the block on all modules in this module store. Creates a snapshot of
     * [modules] under [lock] and executes [block] on that snapshot, so it is
     * guaranteed that the list does not change between [block] calls.
     */
    open fun forEach(block: (it: CommonModule) -> Unit) {
        val snapshot = lock.use {
            modules.toMutableList() // using toMutableList because it guarantees a new list
        }
        snapshot.forEach { block(it) }
    }

    /**
     * Find a dependency between the known dependencies.
     */
    inline fun <reified CONSUMER : Any, reified PROVIDER : Any> dependencies(): List<ModuleDependency<PROVIDER>> =
        dependencies(CONSUMER::class, PROVIDER::class)

    /**
     * Find a dependency between the known dependencies.
     */
    @Suppress("UNCHECKED_CAST")
    open fun <PROVIDER : Any> dependencies(consumer: KClass<out Any>, producer: KClass<out Any>): List<ModuleDependency<PROVIDER>> {
        return (dependencies as List<ModuleDependency<PROVIDER>>).filter { consumer.isInstance(it.consumerModule) && producer == it.providerClass }
    }

}