/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.extend

import io.ktor.routing.*
import zakadabar.stack.util.Unique

/**
 * Base class for backend modules.
 */
abstract class BackendModule : Unique {

    /**
     * Install route handlers.
     *
     * @param  route  Ktor Route context for installing routes
     */
    open fun install(route: Route) = Unit

    /**
     * A function that is called when the module is loaded.
     *
     * When called the other modules may or may not be loaded.
     */
    open fun onLoad() = Unit

    /**
     * An initialization function that is called during system startup to
     * initialize this extension.
     *
     * When called all modules are loaded and the DB is accessible.
     */
    open fun init() = Unit

    /**
     * A cleanup function that is called during system shutdown to clean up
     * this extension. DB is still accessible at this point.
     */
    fun cleanup() = Unit

}