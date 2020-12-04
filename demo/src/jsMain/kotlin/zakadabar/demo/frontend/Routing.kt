/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.demo.frontend.general.DefaultLayout
import zakadabar.demo.frontend.general.Home
import zakadabar.demo.frontend.general.Login
import zakadabar.demo.frontend.port.Singapore
import zakadabar.demo.frontend.port.Tortuga
import zakadabar.demo.frontend.ship.Ships
import zakadabar.demo.frontend.speed.Speeds
import zakadabar.stack.frontend.application.AppRouting

object Routing : AppRouting(DefaultLayout, Home) {

    init {
        + Ships
        + Speeds
        + Singapore
        + Tortuga
        + Login
    }

}