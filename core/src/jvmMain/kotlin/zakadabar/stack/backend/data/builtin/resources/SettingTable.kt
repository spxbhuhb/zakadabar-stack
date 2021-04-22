/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.data.builtin.resources.SettingFormat

object SettingTable : LongIdTable("settings") {

    val role = reference("role", RoleTable).nullable()
    val format = enumerationByName("format", 20, SettingFormat::class)
    val name = varchar("name", 100)
    val value = text("value")

    fun toDto(row: ResultRow) = SettingDto(
        id = row[id].value,
        role = row[role]?.value,
        format = row[format],
        name = row[name],
        value = row[value]
    )

}