/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.demo.frontend.Home
import zakadabar.demo.frontend.Layout
import zakadabar.demo.frontend.port.Singapore
import zakadabar.demo.frontend.port.Tortuga
import zakadabar.demo.frontend.ship.Ships
import zakadabar.demo.frontend.speed.Speeds
import zakadabar.stack.frontend.application.AppRouting

object Routing : AppRouting(Layout, Home) {

    init {
        + Ships
        + Speeds
        + Singapore
        + Tortuga
    }

}