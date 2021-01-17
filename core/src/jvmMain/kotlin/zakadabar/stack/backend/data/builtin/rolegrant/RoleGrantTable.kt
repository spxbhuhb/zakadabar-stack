/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.rolegrant

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.builtin.principal.PrincipalTable
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.data.builtin.RoleGrantDto

object RoleGrantTable : LongIdTable("role_grants") {

    val principal = reference("principal", PrincipalTable).index()
    val role = reference("role", RoleTable).index()

    fun toDto(row: ResultRow) = RoleGrantDto(
        id = row[id].value,
        principal = row[principal].value,
        role = row[role].value
    )

}