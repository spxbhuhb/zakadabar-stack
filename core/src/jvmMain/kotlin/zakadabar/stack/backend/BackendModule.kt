/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import io.ktor.routing.*
import org.jetbrains.exposed.sql.Table
import zakadabar.stack.backend.data.Sql

interface BackendModule {

    /**
     * Called when the module is loaded. At this point modules are not started yet,
     * so inter-dependencies should not be used.
     */
    fun onModuleLoad() = Unit

    /**
     * Called when the module is started. It is safe to use other modules here.
     */
    fun onModuleStart() = Unit

    /**
     * Install route handlers.
     *
     * @param  route  Ktor Route context for installing routes
     */
    fun onInstallRoutes(route: Route) = Unit

    /**
     * A function that is called when the module is unloaded.
     */
    fun onModuleStop() = Unit

    /**
     * This function registers an SQL table. Call from [onModuleLoad].
     * After all modules are created the [Server] will call [Sql.createMissingTablesAndColumns]
     *
     */
    operator fun Table.unaryPlus() {
        Sql.tables += this
    }
}