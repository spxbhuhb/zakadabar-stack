/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.rolegrant

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.builtin.principal.PrincipalTable
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.data.builtin.account.RoleGrantBo

object RoleGrantTable : LongIdTable("role_grants") {

    val principal = reference("principal", PrincipalTable).index()
    val role = reference("role", RoleTable).index()

    fun toBo(row: ResultRow) = RoleGrantBo(
        id = row[id].entityId(),
        principal = row[principal].entityId(),
        role = row[role].entityId()
    )

}