/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.ship

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.demo.backend.port.PortTable
import zakadabar.demo.backend.speed.SpeedTable
import zakadabar.demo.data.ship.ShipDto
import zakadabar.stack.backend.data.builtin.account.AccountPrivateTable
import zakadabar.stack.backend.data.recordId

object ShipTable : LongIdTable("ships") {

    val name = varchar("name", 20)
    val speed = reference("speed", SpeedTable)
    val captain = reference("captain", AccountPrivateTable)
    val description = varchar("description", 2000)
    val hasPirateFlag = bool("has_flag").clientDefault { false }
    val port = reference("port", PortTable).nullable()

    fun toDto(row: ResultRow) = ShipDto(
        id = row[id].recordId(),
        name = row[name],
        speed = row[speed].recordId(),
        captain = row[captain].recordId(),
        description = row[description],
        hasPirateFlag = row[hasPirateFlag],
        port = row[port]?.recordId()
    )

}