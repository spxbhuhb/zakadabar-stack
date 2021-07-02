/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.backend

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.BCrypt

open class AccountPrivateExposedPa(
    table : AccountPrivateExposedTable = AccountPrivateExposedTableCommon
) : ExposedPaBase<AccountPrivateBo, AccountPrivateExposedTable>(
    table = table
) {
    open fun readByName(name: String) = table.select { table.accountName eq name }.first().toBo()

    internal fun readCredentials(id: EntityId<AccountPrivateBo>) =
        table
            .slice(table.credentials)
            .select { table.id eq id.toLong() }
            .map { it[table.credentials] }
            .firstOrNull()

    internal fun writeCredentials(id: EntityId<AccountPrivateBo>, value: String?) =
        table
            .update({ table.id eq id.toLong() }) {
                it[table.credentials] = value
            }

    override fun ResultRow.toBo() = AccountPrivateBo(
        id = this[table.id].entityId(),
        validated = this[table.validated],
        locked = this[table.locked],
        expired = this[table.expired],
        credentials = null /* do not send out the secret */,
        resetKey = null /* do not send out the secret */,
        resetKeyExpiration = this[table.resetKeyExpiration]?.toKotlinInstant(),
        lastLoginSuccess = this[table.lastLoginSuccess]?.toKotlinInstant(),
        loginSuccessCount = this[table.loginSuccessCount],
        lastLoginFail = this[table.lastLoginFail]?.toKotlinInstant(),
        loginFailCount = this[table.loginFailCount],
        accountName = this[table.accountName],
        fullName = this[table.fullName],
        email = this[table.email],
        phone = this[table.phone],
        displayName = this[table.displayName],
        theme = this[table.theme],
        locale = this[table.locale]
    )

    override fun UpdateBuilder<*>.fromBo(bo: AccountPrivateBo) {
        this[table.validated] = bo.validated
        this[table.locked] = bo.locked
        this[table.expired] = bo.expired
        this[table.credentials] = bo.credentials?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        this[table.resetKey] = bo.resetKey?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        this[table.resetKeyExpiration] = bo.resetKeyExpiration?.toJavaInstant()
        this[table.lastLoginSuccess] = bo.lastLoginSuccess?.toJavaInstant()
        this[table.loginSuccessCount] = bo.loginSuccessCount
        this[table.lastLoginFail] = bo.lastLoginFail?.toJavaInstant()
        this[table.loginFailCount] = bo.loginFailCount
        this[table.accountName] = bo.accountName
        this[table.fullName] = bo.fullName
        this[table.email] = bo.email
        this[table.phone] = bo.phone
        this[table.displayName] = bo.displayName
        this[table.theme] = bo.theme
        this[table.locale] = bo.locale
    }
}

object AccountPrivateExposedTableCommon : AccountPrivateExposedTable()

open class AccountPrivateExposedTable : ExposedPaTable<AccountPrivateBo>(
    tableName = "account_private"
) {

     val validated = bool("validated")
     val locked = bool("locked")
     val expired = bool("expired")
     val credentials = varchar("credentials", 200).nullable()
     val resetKey = varchar("reset_key", 200).nullable()
     val resetKeyExpiration = timestamp("reset_key_expiration").nullable()
     val lastLoginSuccess = timestamp("last_login_success").nullable()
     val loginSuccessCount = integer("login_success_count")
     val lastLoginFail = timestamp("last_login_fail").nullable()
     val loginFailCount = integer("login_fail_count")
     val accountName = varchar("account_name", 50)
     val fullName = varchar("full_name", 100)
     val email = varchar("email", 50)
     val phone = varchar("phone", 20).nullable()
     val displayName = varchar("display_name", 50).nullable()
     val theme = varchar("theme", 50).nullable()
     val locale = varchar("locale", 20)

}