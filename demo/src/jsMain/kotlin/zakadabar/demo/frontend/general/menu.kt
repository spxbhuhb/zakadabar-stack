/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.general

import zakadabar.demo.frontend.R
import zakadabar.demo.frontend.port.Singapore
import zakadabar.demo.frontend.port.Tortuga
import zakadabar.demo.frontend.ship.Ships
import zakadabar.demo.frontend.speed.Speeds
import zakadabar.stack.frontend.builtin.menu.Menu
import zakadabar.stack.frontend.builtin.menu.MenuItem

val menu = Menu {
    className = R.Css.menu

    + column {

        + item(R.title) { Home.open() }

        + CuccItem { this -= this[CuccItem::class] }

        + item(R.Ship.ships) { Ships.openAll() }
        + item(R.Speed.speeds) { Speeds.openAll() }

        + group(R.ports) {
            + group(R.carribean) {
                + item(R.tortuga) { Tortuga.open() }
            }
            + group(R.asia) {
                + item(R.singapore) { Singapore.open() }
            }
        }

        + item(R.Account.login) { Login.open() }
    }
}

class CuccItem(onClick: () -> Unit) : MenuItem("cucc", onClick)


