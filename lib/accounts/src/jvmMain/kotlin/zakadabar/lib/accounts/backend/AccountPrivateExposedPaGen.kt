/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.backend

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.util.BCrypt

/**
 * Exposed based Persistence API for AccountPrivateBo.
 *
 * Generated with Bender at 2021-05-28T07:04:53.297Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 *
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class AccountPrivateExposedPaGen : ExposedPaBase<AccountPrivateBo, AccountPrivateExposedTableGen>(
    table = AccountPrivateExposedTableGen
) {
    override fun ResultRow.toBo() = AccountPrivateBo(
        id = this[table.id].entityId(),
        validated = this[AccountPrivateExposedTableGen.validated],
        locked = this[AccountPrivateExposedTableGen.locked],
        expired = this[AccountPrivateExposedTableGen.expired],
        credentials = null /* do not send out the secret */,
        resetKey = null /* do not send out the secret */,
        resetKeyExpiration = this[AccountPrivateExposedTableGen.resetKeyExpiration]?.toKotlinInstant(),
        lastLoginSuccess = this[AccountPrivateExposedTableGen.lastLoginSuccess]?.toKotlinInstant(),
        loginSuccessCount = this[AccountPrivateExposedTableGen.loginSuccessCount],
        lastLoginFail = this[AccountPrivateExposedTableGen.lastLoginFail]?.toKotlinInstant(),
        loginFailCount = this[AccountPrivateExposedTableGen.loginFailCount],
        accountName = this[AccountPrivateExposedTableGen.accountName],
        fullName = this[AccountPrivateExposedTableGen.fullName],
        email = this[AccountPrivateExposedTableGen.email],
        phone = this[AccountPrivateExposedTableGen.phone],
        displayName = this[AccountPrivateExposedTableGen.displayName],
        theme = this[AccountPrivateExposedTableGen.theme],
        locale = this[AccountPrivateExposedTableGen.locale]
    )

    override fun UpdateBuilder<*>.fromBo(bo: AccountPrivateBo) {
        this[AccountPrivateExposedTableGen.validated] = bo.validated
        this[AccountPrivateExposedTableGen.locked] = bo.locked
        this[AccountPrivateExposedTableGen.expired] = bo.expired
        this[AccountPrivateExposedTableGen.credentials] = bo.credentials?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        this[AccountPrivateExposedTableGen.resetKey] = bo.resetKey?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        this[AccountPrivateExposedTableGen.resetKeyExpiration] = bo.resetKeyExpiration?.toJavaInstant()
        this[AccountPrivateExposedTableGen.lastLoginSuccess] = bo.lastLoginSuccess?.toJavaInstant()
        this[AccountPrivateExposedTableGen.loginSuccessCount] = bo.loginSuccessCount
        this[AccountPrivateExposedTableGen.lastLoginFail] = bo.lastLoginFail?.toJavaInstant()
        this[AccountPrivateExposedTableGen.loginFailCount] = bo.loginFailCount
        this[AccountPrivateExposedTableGen.accountName] = bo.accountName
        this[AccountPrivateExposedTableGen.fullName] = bo.fullName
        this[AccountPrivateExposedTableGen.email] = bo.email
        this[AccountPrivateExposedTableGen.phone] = bo.phone
        this[AccountPrivateExposedTableGen.displayName] = bo.displayName
        this[AccountPrivateExposedTableGen.theme] = bo.theme
        this[AccountPrivateExposedTableGen.locale] = bo.locale
    }
}

/**
 * Exposed based SQL table for AccountPrivateBo.
 *
 * Generated with Bender at 2021-05-28T07:04:53.297Z.
 *
 * **IMPORTANT** Please do not modify this class manually.
 *
 * If you need other fields, add them to the business object and then re-generate.
 */
object AccountPrivateExposedTableGen : ExposedPaTable<AccountPrivateBo>(
    tableName = "account_private"
) {

    internal val validated = bool("validated")
    internal val locked = bool("locked")
    internal val expired = bool("expired")
    internal val credentials = varchar("credentials", 200).nullable()
    internal val resetKey = varchar("reset_key", 200).nullable()
    internal val resetKeyExpiration = timestamp("reset_key_expiration").nullable()
    internal val lastLoginSuccess = timestamp("last_login_success").nullable()
    internal val loginSuccessCount = integer("login_success_count")
    internal val lastLoginFail = timestamp("last_login_fail").nullable()
    internal val loginFailCount = integer("login_fail_count")
    internal val accountName = varchar("account_name", 50)
    internal val fullName = varchar("full_name", 100)
    internal val email = varchar("email", 50)
    internal val phone = varchar("phone", 20).nullable()
    internal val displayName = varchar("display_name", 50).nullable()
    internal val theme = varchar("theme", 50).nullable()
    internal val locale = varchar("locale", 20)

}