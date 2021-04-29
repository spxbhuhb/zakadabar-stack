/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.recordId
import zakadabar.stack.data.builtin.resources.LocaleDto

class LocaleDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<LocaleDao>(LocaleTable)

    var name by LocaleTable.name
    var description by LocaleTable.description

    fun toDto() = LocaleDto(
        id = id.recordId(),
        name = name,
        description = description
    )
}