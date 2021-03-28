/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.data.builtin.resources.LocaleStringDto

class LocaleStringDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<LocaleStringDao>(LocaleStringTable)

    var name by LocaleStringTable.name
    var locale by LocaleStringTable.locale
    var value by LocaleStringTable.value

    fun toDto() = LocaleStringDto(
        id = id.value,
        name = name,
        locale = locale,
        value = value
    )
}