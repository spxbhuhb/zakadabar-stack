/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.ship

import zakadabar.demo.marina.data.PortDto
import zakadabar.demo.marina.data.SeaDto
import zakadabar.demo.marina.data.ship.ShipDto
import zakadabar.demo.marina.data.speed.SpeedDto
import zakadabar.demo.marina.resources.Strings
import zakadabar.stack.data.builtin.account.AccountPublicDto
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<ShipDto>() {

    // These preloads are a bit too many, it might be better to create a
    // DTO for this table and build the result properly on server side.

    private val seas by preload { SeaDto.allAsMap() }
    private val ports by preload { PortDto.allAsMap() }
    private val speeds by preload { SpeedDto.allAsMap() }
    private val accounts by preload { AccountPublicDto.allAsMap() }

    override fun onConfigure() {
        titleText = Strings.ships
        crud = Ships

        add = true
        search = true

        + ShipDto::id
        + ShipDto::name
        + ShipDto::hasPirateFlag

        + custom {
            label = Strings.speed
            render = { + speeds[it.speed]?.description }
        }

        + custom {
            label = Strings.captain
            render = { + accounts[it.captain]?.fullName }
        }

        + custom {
            label = Strings.port
            render = {
                val port = ports[it.port]
                val sea = seas[port?.sea]
                + "${port?.name ?: "Davy Jones Locker"} (${sea?.name ?: "At World's End"})"
            }
        }

        + actions()
    }

}