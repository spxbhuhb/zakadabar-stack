/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.account

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.data.account.AccountPrivateDto
import zakadabar.demo.data.account.AccountPublicDto
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.builtin.principal.PrincipalTable

object AccountPrivateTable : LongIdTable("accounts") {

    val principal = reference("principal", PrincipalTable)

    val accountName = varchar("accountName", 50).index() // login finds account by this field
    val fullName = varchar("fullName", 100)
    val displayName = varchar("displayName", 100)
    val avatar = reference("avatar", AccountImageTable).nullable()

    val organizationName = varchar("organizationName", 100)
    val position = varchar("position", 50)

    val email = varchar("email", 50)
    val phone = varchar("phone", 50)

    fun toDto(row: ResultRow) = AccountPrivateDto(
        id = row[id].value,

        accountName = row[accountName],
        fullName = row[fullName],
        displayName = row[displayName],
        avatar = row[avatar]?.value,

        organizationName = row[organizationName],
        position = row[position],

        email = row[email],
        phone = row[phone]
    )

    fun toPublicDto(row: ResultRow) = AccountPublicDto(
        id = row[id].value,

        accountName = row[accountName],
        fullName = row[fullName],
        displayName = row[displayName],
        organizationName = row[organizationName],
        email = row[email]
    )

    /**
     * Get the anonymous account or create one if not exists.
     * This method is called during server startup to make
     * sure there is an anonymous account.
     */
    fun anonymous() = transaction {
        val account = AccountPrivateTable
            .select { accountName eq "anonymous" }
            .map { toPublicDto(it) }
            .firstOrNull()

        if (account != null) return@transaction account

        val newPrincipal = PrincipalDao.new { }

        val newAccount = AccountPrivateDao.new {
            principal = newPrincipal
            accountName = "anonymous"
            fullName = "anonymous"
            displayName = "anonymous"
            organizationName = ""
            position = ""
            email = ""
            phone = ""
        }

        newAccount.toPublicDto()
    }

}