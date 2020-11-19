/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.samples.theplace.frontend.Home
import zakadabar.samples.theplace.frontend.Layout
import zakadabar.samples.theplace.frontend.ports.Singapore
import zakadabar.samples.theplace.frontend.ports.Tortuga
import zakadabar.samples.theplace.frontend.ship.Ships
import zakadabar.samples.theplace.frontend.speed.Speeds
import zakadabar.stack.frontend.application.AppRouting

object Routing : AppRouting(Layout, Home) {

    init {
        + Ships
        + Speeds
        + Singapore
        + Tortuga
    }

}