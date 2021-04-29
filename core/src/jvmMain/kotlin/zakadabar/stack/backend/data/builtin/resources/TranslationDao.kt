/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.recordId
import zakadabar.stack.data.builtin.resources.TranslationDto

class TranslationDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TranslationDao>(TranslationTable)

    var name by TranslationTable.name
    var locale by TranslationTable.locale
    var value by TranslationTable.value

    fun toDto() = TranslationDto(
        id = id.recordId(),
        name = name,
        locale = locale,
        value = value
    )
}