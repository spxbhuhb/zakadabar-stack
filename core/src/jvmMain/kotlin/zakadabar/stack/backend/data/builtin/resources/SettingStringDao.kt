/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.data.builtin.resources.LocaleStringDto
import zakadabar.stack.data.builtin.resources.SettingStringDto

class SettingStringDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SettingStringDao>(SettingStringTable)

    var name by LocaleStringTable.name
    var value by LocaleStringTable.value

    fun toDto() = SettingStringDto(
        id = id.value,
        name = name,
        value = value
    )
}