/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import kotlinx.browser.window
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import zakadabar.demo.data.PortDto
import zakadabar.demo.data.SeaDto
import zakadabar.demo.frontend.pages.account.Accounts
import zakadabar.demo.frontend.pages.builtin.ArgPage
import zakadabar.demo.frontend.pages.builtin.CheckboxList
import zakadabar.demo.frontend.pages.builtin.FormFields
import zakadabar.demo.frontend.pages.builtin.TabContainer
import zakadabar.demo.frontend.pages.misc.Home
import zakadabar.demo.frontend.pages.misc.Login
import zakadabar.demo.frontend.pages.port.Ports
import zakadabar.demo.frontend.pages.sea.Seas
import zakadabar.demo.frontend.pages.ship.ShipSearch
import zakadabar.demo.frontend.pages.ship.Ships
import zakadabar.demo.frontend.pages.speed.Speeds
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.data.builtin.LogoutAction
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.fourRandomInt

object SideBar : ZkSideBar() {

    init {
        style {
            height = "100%"
            backgroundImage = """url("/menu_background.jpg")"""
            backgroundSize = "cover"
        }

        + title(Strings.applicationName, ::hideMenu) { Home.open() }

        + item(Strings.search) { ShipSearch.open() }

        + item(Strings.ships) { Ships.openAll() }
        + item(Strings.speeds) { Speeds.openAll() }

        + seasAndPorts()

        + group(Strings.builtin) {
            + item("ZkArgPage") { ArgPage.open(ArgPage.Args(fourRandomInt()[0], "hello")) }
            + item("ZkCheckboxList") { CheckboxList.open() }
            + item("ZkForm") { FormFields.open() }
            + item("ZkTabContainer") { TabContainer.open() }
        }

        // show administration only for logged in users

        ifNotAnonymous {
            + group(Strings.administration) {
                + item(Strings.seas) { Seas.openAll() }
                + item(Strings.ports) { Ports.openAll() }
                + item(Strings.accounts) { Accounts.openAll() }
            }
        }

        ifAnonymous {
            + item(Strings.login) { Login.open() }
        }

        ifNotAnonymous {
            + item(Strings.logout) {
                io {
                    LogoutAction().execute()
                    window.location.href = window.location.href // to refresh page
                }
            }
        }
    }

    private fun hideMenu() {

    }

    /**
     * This function is a bit tricky and not well optimized but it works.
     * What happens here is that we download the list of seas and
     * ports from the server and build the menu from the actual DB content.
     *
     * This is not perfect as the page needs refresh to update the menu.
     */
    private fun seasAndPorts() = group(Strings.ports) {

        // This part is in a launch, that means ports will be empty until
        // the data arrives. It would be a bit better to show the user that
        // we are downloading.

        io {
            var seas = emptyList<SeaDto>()
            var ports = emptyList<PortDto>()

            // This pattern lets us download the seas and ports simultaneously.
            // the coroutineScope ends when both of the launched jobs are done.

            coroutineScope {
                launch { seas = SeaDto.all() }
                launch { ports = PortDto.all() }
            }

            seas.forEach { sea ->
                + group(sea.name) {
                    ports.filter { it.sea == sea.id }.forEach { port ->
                        + item(port.name) {
                            Ports.openUpdate(port.id)
                        }
                    }
                }
            }

        }
    }

}



