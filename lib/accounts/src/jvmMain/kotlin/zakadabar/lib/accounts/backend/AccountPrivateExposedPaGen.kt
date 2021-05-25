/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.exposed.ExposedPaBase
import zakadabar.stack.backend.data.exposed.ExposedPaTable

/**
 * `Exposed` based Persistence API for ${boName}.
 *
 * Generated with Bender at ${Clock.System.now()}.
 *
 * **IMPORTANT**
 *
 * ```
 * Please do not modify this class, see extending patterns below.
 * ```
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class AccountPrivateExposedPaGen : ExposedPaBase<AccountPrivateBo>(AccountPrivateExposedTable())

class AccountPrivateExposedTable : ExposedPaTable<AccountPrivateBo>("account_private") {

    val principal = reference("principal", PrincipalExposedTable)
    val accountName = varchar("account_name", 50)
    val fullName = varchar("full_name", 100)
    val email = varchar("email", 50)
    val displayName = varchar("display_name", 50).nullable()
    val locale = varchar("locale", 20)
    val theme = varchar("theme", 50).nullable()
    val organizationName = varchar("organization_name", 100).nullable()
    val position = varchar("position", 50).nullable()
    val phone = varchar("phone", 20).nullable()

    override fun toBo(row: ResultRow) = AccountPrivateBo(
        id = row[id].entityId(),
        principal = row[principal].entityId(),
        accountName = row[accountName],
        fullName = row[fullName],
        email = row[email],
        displayName = row[displayName],
        locale = row[locale],
        theme = row[theme],
        organizationName = row[organizationName],
        position = row[position],
        phone = row[phone]
    )

    override fun fromBo(statement: UpdateBuilder<*>, bo: AccountPrivateBo) {
        statement[principal] = bo.principal.toLong()
        statement[accountName] = bo.accountName
        statement[fullName] = bo.fullName
        statement[email] = bo.email
        statement[displayName] = bo.displayName
        statement[locale] = bo.locale
        statement[theme] = bo.theme
        statement[organizationName] = bo.organizationName
        statement[position] = bo.position
        statement[phone] = bo.phone
    }

}

