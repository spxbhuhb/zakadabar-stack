/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.account

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.builtin.principal.PrincipalTable
import zakadabar.stack.data.builtin.account.AccountPrivateDto
import zakadabar.stack.data.builtin.account.AccountPublicDto

object AccountPrivateTable : LongIdTable("accounts") {

    val principal = reference("principal", PrincipalTable)

    val accountName = varchar("accountName", 50).index() // login finds account by this field
    val fullName = varchar("fullName", 100)
    val email = varchar("email", 50)

    val displayName = varchar("displayName", 100).nullable()
    val theme = varchar("theme", 50).nullable()
    val locale = varchar("locale", 20).nullable()
    val avatar = reference("avatar", AccountImageTable).nullable()

    val organizationName = varchar("organizationName", 100).nullable()
    val position = varchar("position", 50).nullable()
    val phone = varchar("phone", 50).nullable()

    fun toDto(row: ResultRow) = AccountPrivateDto(
        id = row[id].value,

        accountName = row[accountName],
        fullName = row[fullName],
        email = row[email],

        displayName = row[displayName],
        theme = row[theme],
        locale = row[locale],
        avatar = row[avatar]?.value,

        organizationName = row[organizationName],
        position = row[position],
        phone = row[phone]
    )

    fun toPublicDto(row: ResultRow, addEmail: Boolean = false) = AccountPublicDto(
        id = row[id].value,

        accountName = row[accountName],
        fullName = row[fullName],
        email = if (addEmail) row[email] else null,

        displayName = row[displayName],
        theme = row[theme],
        locale = row[locale],

        organizationName = row[organizationName]
    )

}