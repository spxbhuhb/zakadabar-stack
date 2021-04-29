/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.rolegrant

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.builtin.principal.PrincipalTable
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.recordId
import zakadabar.stack.data.builtin.account.RoleGrantDto

object RoleGrantTable : LongIdTable("role_grants") {

    val principal = reference("principal", PrincipalTable).index()
    val role = reference("role", RoleTable).index()

    fun toDto(row: ResultRow) = RoleGrantDto(
        id = row[id].recordId(),
        principal = row[principal].recordId(),
        role = row[role].recordId()
    )

}