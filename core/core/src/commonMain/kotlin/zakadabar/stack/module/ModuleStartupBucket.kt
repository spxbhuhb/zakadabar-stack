/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.module

/**
 * Startup buckets define the order in which modules start and stop.
 * [ModuleStore.buckets] contains the list of buckets.
 *
 * Buckets are ordered by their [order] property: lower the order,
 * earlier the modules start.
 *
 * There are three pre-defined buckets, started in this order:
 *
 * - [BOOTSTRAP] modules needed for other modules to load properly, SQL providers for example
 * - [DEFAULT] normal business level modules
 * - [PUBLISH] modules that accept incoming connections, Ktor for example
 * - [STEADY] modules to be started after the system becomes steady
 *
 * To add a bucket simply add the instance to the module store as
 * below.
 *
 * ```kotlin
 * modules += ModuleStartupBucket(100)
 * ```
 *
 * You can specify a bucket for a module by adding one of these interfaces:
 *
 * ```kotlin
 * class ExampleBootstrapModule() : CommonModule, BootstrapBucket
 * class ExamplePublishModule() : CommonModule, PublishBucket
 * class ExampleSteadyModule() : CommonModule, SteadyBucket
 * ```
 *
 * You can specify the bucket directly when you add a module. This overrides
 * the bucket specified in module class (see below).
 *
 * ```kotlin
 * modules += ExampleBl() to ModuleStartupBucket.BOOTSTRAP
 * ```
 */
open class ModuleStartupBucket(
    val order : Int,
    val selector : (CommonModule) -> Boolean
) {

    protected val modules = mutableListOf<CommonModule>()

    operator fun plusAssign(module : CommonModule) {
        modules += module
    }

    /**
     * Call `onInitializeDb` for each module.
     */
    open fun initializeDb(store : ModuleStore) {
        modules.forEach {
            try {
                it.onInitializeDb()
                store.logger.debug("initialized DB for module $it")
            } catch (ex: Throwable) {
                store.logger.error("failed to initialize DB for module $it", ex)
                throw ex
            }
        }
    }

    /**
     * Call `onModuleStart` for each module.
     */
    open fun start(store : ModuleStore) {
        modules.forEach {
            try {
                it.onModuleStart()
                store.logger.info("started module $it")
            } catch (ex: Throwable) {
                store.logger.error("failed to start module $it", ex)
                throw ex
            }
        }
    }

    /**
     * Call `onModuleStop` for all modules.
     */
    open fun stop(store : ModuleStore) {
        modules.forEach {
            try {
                it.onModuleStop()
                store.logger.info("stopped module $it")
            } catch (ex: Throwable) {
                store.logger.error("failed to stop module $it", ex)
            }
        }

    }

    /**
     * Remove all modules from this bucket.
     */
    open fun clear() {
        modules.clear()
    }

    companion object {
        const val BOOTSTRAP = Int.MIN_VALUE / 2
        const val DEFAULT = 0
        const val PUBLISH = Int.MAX_VALUE / 4
        const val STEADY = Int.MAX_VALUE / 2

        interface BootstrapBucket
        interface PublishBucket
        interface SteadyBucket
    }
}
