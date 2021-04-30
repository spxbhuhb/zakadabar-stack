/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.backend.sea

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.marina.data.SeaDto
import zakadabar.stack.backend.data.recordId

class SeaDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SeaDao>(SeaTable)

    var name by SeaTable.name

    fun toDto() = SeaDto(
        id = id.recordId(),
        name = name
    )
}