/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import Routing
import zakadabar.stack.frontend.application.navigation.navLink
import zakadabar.stack.frontend.elements.ZkElement

object Menu : ZkElement() {

    private val content = ZkElement()

    override fun init() = build {
        + column {
            + row {
                with(Routing) {
                    + navLink(ships, "Ships")
                    + navLink(tortuga, "Tortuga")
                    + navLink(singapore, "Singapore")
                }
            }
            + content
        }
    }

}