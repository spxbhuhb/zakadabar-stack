/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.builtin.resources.LocaleStringDto

object LocaleStringTable : LongIdTable("locale_strings") {

    val name = varchar("name", 100)
    val locale = varchar("locale", 20)
    val value = text("value")

    fun toDto(row: ResultRow) = LocaleStringDto(
        id = row[id].value,
        name = row[name],
        locale = row[locale],
        value = row[value]
    )

}