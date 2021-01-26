/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.ship

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.backend.account.account.AccountPrivateDao
import zakadabar.demo.backend.port.PortDao
import zakadabar.demo.backend.speed.SpeedDao
import zakadabar.demo.data.ShipDto

class ShipDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ShipDao>(ShipTable)

    var name by ShipTable.name
    var speed by SpeedDao referencedOn ShipTable.speed
    var captain by AccountPrivateDao referencedOn ShipTable.captain
    var description by ShipTable.description
    var hasFlag by ShipTable.hasPirateFlag
    var port by PortDao optionalReferencedOn ShipTable.port

    fun toDto() = ShipDto(
        id = id.value,
        name = name,
        speed = speed.id.value,
        captain = captain.id.value,
        description = description,
        hasPirateFlag = hasFlag,
        port = port?.id?.value
    )
}