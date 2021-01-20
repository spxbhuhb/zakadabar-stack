/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.data.builtin.AccountPublicDto
import zakadabar.stack.frontend.builtin.table.ZkTable
import kotlin.reflect.KMutableProperty1

class Table : ZkTable<ShipDto>() {

    private val speeds by preload { SpeedDto.allAsMap() }
    private val accounts by preload { AccountPublicDto.allAsMap() }

    init {
        title = Strings.ships
        onCreate = { Ships.openCreate() }
        onSearch = { }

        + ShipDto::id build { label = "#" }
        + ShipDto::name
        + ShipDto::hasFlag

        + custom {
            label = Strings.speed
            render = { + speeds[it.speed]?.description }
        }

        + custom {
            label = Strings.captain
            render = { + accounts[it.captain]?.fullName }
        }

        + ShipDto::id.actions(Ships)
    }

}