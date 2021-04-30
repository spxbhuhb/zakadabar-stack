/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.lib.backend.builtin

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.lib.data.builtin.ExampleReferenceDto
import zakadabar.stack.backend.data.recordId

class ExampleReferenceDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ExampleReferenceDao>(ExampleReferenceTable)

    var name by ExampleReferenceTable.name

    fun toDto() = ExampleReferenceDto(
        id = id.recordId(),
        name = name
    )

    fun fromDto(dto: ExampleReferenceDto): ExampleReferenceDao {
        name = dto.name
        return this
    }

}