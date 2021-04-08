/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.builtin

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.data.builtin.ExampleReferenceDto

class ExampleReferenceDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ExampleReferenceDao>(ExampleReferenceTable)

    var name by ExampleReferenceTable.name

    fun toDto() = ExampleReferenceDto(
        id = id.value,
        name = name
    )

    fun fromDto(dto: ExampleReferenceDto): ExampleReferenceDao {
        name = dto.name
        return this
    }

}