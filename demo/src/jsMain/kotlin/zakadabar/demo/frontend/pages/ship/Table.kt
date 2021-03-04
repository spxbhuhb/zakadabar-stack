/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.PortDto
import zakadabar.demo.data.SeaDto
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.data.ship.ShipDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.data.builtin.AccountPublicDto
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<ShipDto>() {

    // These preloads are a bit too many, it might be better to create a
    // DTO for this table and build the result properly on server side.

    private val seas by preload { SeaDto.allAsMap() }
    private val ports by preload { PortDto.allAsMap() }
    private val speeds by preload { SpeedDto.allAsMap() }
    private val accounts by preload { AccountPublicDto.allAsMap() }

    init {
        title = Strings.ships
        crud = Ships
        onSearch = { }

        + ShipDto::id build { label = "#" }
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