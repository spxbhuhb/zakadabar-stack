/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.lib.examples.data.SimpleExampleDto
import zakadabar.stack.backend.data.entityId

class SimpleExampleDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SimpleExampleDao>(SimpleExampleTable)

    var name by SimpleExampleTable.name

    fun toDto() = SimpleExampleDto(
        id = id.entityId(),
        name = name
    )

    fun fromDto(dto : SimpleExampleDto) : SimpleExampleDao {
        name = dto.name
        return this
    }
}