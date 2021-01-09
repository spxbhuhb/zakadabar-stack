/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.rolegrant

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.demo.backend.account.account.AccountTable
import zakadabar.demo.backend.account.role.RoleTable
import zakadabar.demo.data.account.RoleGrantDto

object RoleGrantTable : LongIdTable("role_grants") {

    val account = reference("account", AccountTable).index()
    val role = reference("role", RoleTable).index()

    fun toDto(row: ResultRow) = RoleGrantDto(
        id = row[id].value,
        account = row[account].value,
        role = row[role].value
    )

}