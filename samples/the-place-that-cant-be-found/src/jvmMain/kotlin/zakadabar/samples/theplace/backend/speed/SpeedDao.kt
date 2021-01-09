/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.backend.speed

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.samples.theplace.data.SpeedDto

class SpeedDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SpeedDao>(SpeedTable)

    var description by SpeedTable.description
    var value by SpeedTable.value

    fun toDto() = SpeedDto(
        id = id.value,
        description = description,
        value = value
    )
}