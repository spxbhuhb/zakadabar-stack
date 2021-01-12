/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import io.ktor.routing.*
import org.slf4j.LoggerFactory

/**
 * Base class for custom backends. Supports CRUD, queries and BLOBs.
 */
abstract class CustomBackend : BackendModule {

    protected val logger = LoggerFactory.getLogger(this::class.simpleName) !!

    /**
     * Install route handlers.
     *
     * @param  route  Ktor Route context for installing routes
     */
    open fun install(route: Route) = Unit

    /**
     * An initialization function that is called during system startup to
     * initialize this module.
     *
     * When called all modules are loaded and the DB is accessible.
     */
    override fun init() = Unit

    /**
     * A cleanup function that is called during system shutdown to clean up
     * this module. DB is still accessible at this point.
     */
    override fun cleanup() = Unit

}