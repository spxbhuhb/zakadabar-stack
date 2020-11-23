/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.demo.frontend.port.Singapore
import zakadabar.demo.frontend.port.Tortuga
import zakadabar.demo.frontend.ship.Ships
import zakadabar.demo.frontend.speed.Speeds
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkElement.Companion.buildNew

val Menu = buildNew {

    val self = this.zkElement

    + column {
        + row {
            + SimpleButton("<") { self.hide() }
            + "The Place"
        }
        + SimpleButton("Ships") { Ships.openAll() }
        + SimpleButton("Speeds") { Speeds.openAll() }
        + SimpleButton("Tortuga") { Tortuga.open() }
        + SimpleButton("Singapore") { Singapore.open() }
    }

}