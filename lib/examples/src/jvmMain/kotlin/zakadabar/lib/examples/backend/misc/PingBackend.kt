/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.misc

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import zakadabar.stack.backend.RoutedModule

object PingBackend : RoutedModule {

    override fun onInstallRoutes(route: Any) {
        route as Route
        with(route) {
            get("ping") {
                call.respond("pong")
            }
        }
    }

}
