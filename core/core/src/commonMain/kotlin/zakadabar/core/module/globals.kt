/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.module

/**
 * Modules of the application. This store is used both for backend and
 * frontend to store general application modules.
 */
val modules = ModuleStore()

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
inline fun <reified T : Any> module(noinline selector: (T) -> Boolean = { true }) = ModuleDependencyProvider(T::class, selector)


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
inline fun <reified T : Any> optionalModule(noinline selector: (T) -> Boolean = { true }) = OptionalModuleDependencyProvider(T::class, selector)
