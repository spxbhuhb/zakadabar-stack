/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.ship

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.backend.port.PortDao
import zakadabar.demo.backend.speed.SpeedDao
import zakadabar.demo.data.ship.ShipDto
import zakadabar.stack.backend.data.builtin.account.AccountPrivateDao
import zakadabar.stack.backend.data.recordId

class ShipDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ShipDao>(ShipTable)

    var name by ShipTable.name
    var speed by SpeedDao referencedOn ShipTable.speed
    var captain by AccountPrivateDao referencedOn ShipTable.captain
    var description by ShipTable.description
    var hasFlag by ShipTable.hasPirateFlag
    var port by PortDao optionalReferencedOn ShipTable.port

    fun toDto() = ShipDto(
        id = id.recordId(),
        name = name,
        speed = speed.id.recordId(),
        captain = captain.id.recordId(),
        description = description,
        hasPirateFlag = hasFlag,
        port = port?.id?.recordId()
    )
}