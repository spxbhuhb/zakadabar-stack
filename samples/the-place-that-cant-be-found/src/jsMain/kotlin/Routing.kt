/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.samples.theplace.frontend.Home
import zakadabar.samples.theplace.frontend.Ships
import zakadabar.samples.theplace.frontend.Singapore
import zakadabar.samples.theplace.frontend.Tortuga
import zakadabar.stack.frontend.application.AppRouting
import zakadabar.stack.frontend.builtin.layout.FullScreen

object Routing : AppRouting(FullScreen) {

    init {
        + Home
        + Ships
        + Singapore
        + Tortuga
    }

}