/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.misc

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import zakadabar.stack.backend.custom.CustomBackend

object PingBackend : CustomBackend() {

    override fun install(route: Route) {
        with(route) {
            get("ping") {
                call.respond("pong")
            }
        }
    }

}
