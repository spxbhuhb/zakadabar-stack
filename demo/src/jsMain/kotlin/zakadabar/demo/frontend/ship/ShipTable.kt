/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.stack.frontend.builtin.table.Table

@Suppress("unused") // table pattern
class ShipTable : Table<ShipDto>() {

    private val speeds by preload { SpeedDto.allAsMap() }

    val recordId by column(ShipDto::id)
    val description by column(ShipDto::name)
    val value by column { + speeds[it.speed]?.description }
    val update by updateLink(ShipDto::id, Ships)

}