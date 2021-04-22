/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.builtin.role.RoleDao
import zakadabar.stack.data.builtin.resources.SettingDto

class SettingDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SettingDao>(SettingTable)

    val role by RoleDao optionalReferencedOn SettingTable.role
    val format by SettingTable.format
    var name by SettingTable.name
    var value by SettingTable.value

    fun toDto() = SettingDto(
        id = id.value,
        role = role?.id?.value,
        format = format,
        name = name,
        value = value
    )
}