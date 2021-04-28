/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.data.builtin.resources.SettingSource

object SettingTable : LongIdTable("settings") {

    val role = reference("role", RoleTable).nullable()
    var namespace = varchar("namespace", 100)
    val className = varchar("path", 100)
    val value = text("value")

    fun toDto(row: ResultRow) = SettingDto(
        id = row[id].value,
        role = row[role]?.value,
        source = SettingSource.Database,
        namespace = row[namespace],
        className = row[className],
        value = row[value]
    )

}