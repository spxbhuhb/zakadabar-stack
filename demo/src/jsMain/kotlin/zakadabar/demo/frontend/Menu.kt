/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.demo.frontend.pages.account.Accounts
import zakadabar.demo.frontend.pages.misc.Home
import zakadabar.demo.frontend.pages.misc.Login
import zakadabar.demo.frontend.pages.port.Singapore
import zakadabar.demo.frontend.pages.port.Tortuga
import zakadabar.demo.frontend.pages.ship.ShipSearch
import zakadabar.demo.frontend.pages.ship.Ships
import zakadabar.demo.frontend.pages.speed.Speeds
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.menu.ZkMenu

object Menu : ZkMenu({

    + column {

        + item(Strings.applicationName) { Home.open() }

        + item(Strings.search) { ShipSearch.open() }

        + item(Strings.ships) { Ships.openAll() }
        + item(Strings.speeds) { Speeds.openAll() }

        + group(Strings.ports) {
            + group(Strings.carribean) {
                + item(Strings.tortuga) { Tortuga.open() }
            }
            + group(Strings.asia) {
                + item(Strings.singapore) { Singapore.open() }
            }
        }

        + item(Strings.accounts) { Accounts.openAll() }

        + item(Strings.login) { Login.open() }
    }

})

