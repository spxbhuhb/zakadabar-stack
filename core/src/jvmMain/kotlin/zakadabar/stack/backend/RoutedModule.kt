/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import zakadabar.stack.module.CommonModule

@Deprecated("EOL: 2021.8.1  -  use RoutedModule instead", ReplaceWith("RoutedModule"))
interface BackendModule : RoutedModule

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