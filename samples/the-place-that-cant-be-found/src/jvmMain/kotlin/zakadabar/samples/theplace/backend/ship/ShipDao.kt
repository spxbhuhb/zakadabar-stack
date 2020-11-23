/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.backend.ship

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.samples.theplace.backend.speed.SpeedDao
import zakadabar.samples.theplace.data.ShipDto

class ShipDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ShipDao>(ShipTable)

    var name by ShipTable.name
    var speed by SpeedDao referencedOn ShipTable.speed

    fun toDto() = ShipDto(
        id = id.value,
        name = name,
        speed = speed.id.value
    )
}