/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.samples.theplace.frontend.*
import zakadabar.stack.frontend.application.AppRouting

object Routing : AppRouting(Layout, Home) {

    init {
        + Ships
        + Singapore
        + Tortuga
    }

}