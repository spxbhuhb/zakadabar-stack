/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.builtin.resources.LocaleStringDto
import zakadabar.stack.data.builtin.resources.SettingStringDto

object SettingStringTable : LongIdTable("setting_strings") {

    val name = varchar("name", 100)
    val value = text("value")

    fun toDto(row: ResultRow) = SettingStringDto(
        id = row[id].value,
        name = row[name],
        value = row[value]
    )

}