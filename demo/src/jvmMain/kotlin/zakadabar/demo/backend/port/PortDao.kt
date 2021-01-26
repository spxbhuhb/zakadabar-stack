/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.port

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.backend.sea.SeaDao
import zakadabar.demo.data.PortDto

class PortDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PortDao>(PortTable)

    var name by PortTable.name
    var sea by SeaDao referencedOn PortTable.sea

    fun toDto() = PortDto(
        id = id.value,
        name = name,
        sea = sea.id.value
    )
}