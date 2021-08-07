/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import zakadabar.stack.module.CommonModule

interface RoutedModule : CommonModule {

    /**
     * Install route handlers.
     *
     * @param  route  Route context for installing routes. For Ktor this is a Route.
     */
    fun onInstallRoutes(route: Any) = Unit

    /**
     * Install static routes.
     *
     * @param  route  Route context for installing routes. For Ktor this is a Route.
     */
    fun onInstallStatic(route: Any) = Unit

}