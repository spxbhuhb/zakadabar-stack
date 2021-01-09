/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.account

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.demo.data.account.AccountDto

object AccountTable : LongIdTable("accounts") {

    val accountName = varchar("accountName", 100)
    val email = varchar("email", 100)
    val fullName = varchar("fullName", 100)
    val displayName = varchar("displayName", 100).nullable()
    val organizationName = varchar("organizationName", 100).nullable()
    val avatar = reference("avatar", AccountImageTable).nullable()

    fun toDto(row: ResultRow) = AccountDto(
        id = row[id].value,
        email = row[email],
        fullName = row[fullName],
        displayName = row[displayName],
        organizationName = row[organizationName],
        avatar = row[avatar]?.value
    )

}