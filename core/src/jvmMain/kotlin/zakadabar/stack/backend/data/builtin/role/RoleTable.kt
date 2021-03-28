/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.role

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.data.builtin.account.RoleDto

object RoleTable : LongIdTable("roles") {

    val name = varchar("name", 100)
    val description = varchar("description", 4000)

    fun toDto(row: ResultRow) = RoleDto(
        id = row[id].value,
        name = row[name],
        description = row[description]
    )

}