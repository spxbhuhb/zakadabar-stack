/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.module

/**
 * A general module interface to be used both on frontend and backend.
 */
interface CommonModule {

    /**
     * Called when the module is loaded. At this point modules are not started yet,
     * so inter-dependencies should not be used.
     */
    fun onModuleLoad() = Unit

    /**
     * Called after all modules are loaded, all SQL tables are created, but before
     * any modules are started.
     */
    fun onInitializeDb() = Unit

    /**
     * Called when the module is started. It is safe to use other modules here.
     */
    fun onModuleStart() = Unit

    /**
     * A function that is called when the module is unloaded.
     */
    fun onModuleStop() = Unit

}