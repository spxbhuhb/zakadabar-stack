/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.misc

import io.ktor.routing.*
import io.ktor.websocket.*
import zakadabar.stack.backend.BackendModule

object WebsocketBackend : BackendModule {

    override fun onInstallRoutes(route: Route) {
        with(route) {
            webSocket("ws") { // available at /api/ws
                println("connection to ws")
            }
        }
    }

}
