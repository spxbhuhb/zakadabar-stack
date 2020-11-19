/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.backend.ship

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.samples.theplace.ThePlace
import zakadabar.samples.theplace.backend.speed.SpeedTable
import zakadabar.samples.theplace.data.ShipDto

object ShipTable : LongIdTable("t_${ThePlace.shid}_ships") {

    val name = varchar("name", 20)
    val speed = reference("speed", SpeedTable)

    fun toDto(row: ResultRow) = ShipDto(
        id = row[id].value,
        name = row[name],
        speed = row[speed].value
    )

}