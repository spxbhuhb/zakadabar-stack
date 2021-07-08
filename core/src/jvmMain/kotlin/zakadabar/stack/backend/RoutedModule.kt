/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import io.ktor.routing.*
import zakadabar.stack.module.CommonModule

@Deprecated(
    "EOL: 2021.8.1  -  use RoutedModule instead, use \"if(route is Route)\" or \"route as Route\" for Ktor specific routing",
    ReplaceWith("RoutedModule"),
)
interface BackendModule : RoutedModule {

    fun onInstallRoutes(route: Route) {
        onInstallRoutes(route as Any)
    }

    fun onInstallStatic(route: Route) {
        onInstallStatic(route as Any)
    }

}

interface RoutedModule : CommonModule {

    /**
     * Install route handlers.
     *
     * @param  route  Ktor Route context for installing routes
     */
    fun onInstallRoutes(route: Any) = Unit

    /**
     * Install static routes.
     */
    fun onInstallStatic(route: Any) = Unit

}