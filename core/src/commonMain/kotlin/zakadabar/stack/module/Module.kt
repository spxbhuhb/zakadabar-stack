/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.module

import zakadabar.stack.util.InstanceStore

/**
 * Modules of the application. This store is used both for backend and
 * frontend to store general application modules.
 */
val modules = InstanceStore<Module>()

/**
 * Common interface for modules, backend and frontend.
 */
interface Module {

    /**
     * Called when the module is loaded. At this point modules are not started yet,
     * so inter-module dependencies should not be used.
     */
    fun onModuleLoad() = Unit

    /**
     * Called when the module is started. It is safe to use other modules here.
     */
    fun onModuleStart() = Unit

    /**
     * A function that is called when the module is stopped.
     */
    fun onModuleStop() = Unit

}