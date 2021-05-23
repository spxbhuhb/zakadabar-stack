/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.role

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.data.builtin.account.RoleBo

object RoleTable : LongIdTable("roles") {

    val name = varchar("name", 100)
    val description = varchar("description", 4000)

    fun toBo(row: ResultRow) = RoleBo(
        id = row[id].entityId(),
        name = row[name],
        description = row[description]
    )

}