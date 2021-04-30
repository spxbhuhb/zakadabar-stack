/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend

import kotlinx.browser.window
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import zakadabar.demo.marina.data.PortDto
import zakadabar.demo.marina.data.SeaDto
import zakadabar.demo.marina.frontend.pages.port.Ports
import zakadabar.demo.marina.frontend.pages.sea.Seas
import zakadabar.demo.marina.frontend.pages.ship.ShipSearch
import zakadabar.demo.marina.frontend.pages.ship.Ships
import zakadabar.demo.marina.frontend.pages.speed.Speeds
import zakadabar.demo.marina.resources.Strings
import zakadabar.stack.data.builtin.account.LogoutAction
import zakadabar.stack.frontend.builtin.pages.account.accounts.Accounts
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io

object SideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + item(Strings.search) { ShipSearch.open() }

        + item(Strings.ships) { Ships.openAll() }
        + item(Strings.speeds) { Speeds.openAll() }

        + seasAndPorts()

        // show administration only for logged in users

        ifNotAnonymous {
            + group(Strings.administration) {
                + item(Strings.seas) { Seas.openAll() }
                + item(Strings.ports) { Ports.openAll() }
                + item(Strings.accounts) { Accounts.openAll() }
            }
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



