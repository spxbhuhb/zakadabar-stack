/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class ShipTable : ZkTable<ShipDto>() {

    private val speeds by preload { SpeedDto.allAsMap() }

    init {
        title = Strings.Ship.ships
        onCreate = { Ships.openCreate() }

        + ShipDto::id build { label = "#" }
        + ShipDto::name

        + custom {
            label = Strings.Speed.speed
            render = { + speeds[it.speed]?.description }
        }

        + ShipDto::id.actions(Ships)
    }

}