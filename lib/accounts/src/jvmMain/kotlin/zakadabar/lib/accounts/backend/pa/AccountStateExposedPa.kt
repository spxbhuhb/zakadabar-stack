/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.backend.pa

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.lib.accounts.data.AccountStateBo
import zakadabar.core.persistence.exposed.LinkedExposedPaBase
import zakadabar.core.persistence.exposed.LinkedExposedPaTable
import zakadabar.core.persistence.exposed.entityId
import zakadabar.core.util.after

open class AccountStateExposedPa(
    table: AccountStateExposedTable = AccountStateExposedTable()
) : LinkedExposedPaBase<AccountStateBo, AccountPrivateBo, AccountStateExposedTable>(
    table
) {

    override fun linkedId(bo: AccountStateBo) = bo.accountId

    override fun ResultRow.toBo() = AccountStateBo(
        accountId = this[table.entityId].entityId(),
        validated = this[table.validated],
        locked = this[table.locked],
        expired = this[table.expired],
        anonymized = this[table.anonymized],
        lastLoginSuccess = this[table.lastLoginSuccess]?.toKotlinInstant(),
        loginSuccessCount = this[table.loginSuccessCount],
        lastLoginFail = this[table.lastLoginFail]?.toKotlinInstant(),
        loginFailCount = this[table.loginFailCount],
    )

    override fun UpdateBuilder<*>.fromBo(bo: AccountStateBo) {
        this[table.entityId] = bo.accountId.toLong()
        this[table.validated] = bo.validated
        this[table.locked] = bo.locked
        this[table.expired] = bo.expired
        this[table.anonymized] = bo.anonymized
        this[table.lastLoginSuccess] = bo.lastLoginSuccess?.toJavaInstant()
        this[table.loginSuccessCount] = bo.loginSuccessCount
        this[table.lastLoginFail] = bo.lastLoginFail?.toJavaInstant()
        this[table.loginFailCount] = bo.loginFailCount
    }
}

/**
 * Stores the state of the account. Default table name is `account_state`.
 *
 * @property  entityId          The id of the [AccountPrivateBo] BO this credentials belong to.
 * @property  validated         True when this account is validated y email, by
 *                              other means, or when validation is switched off.
 *                              Prevents login when false.
 * @property  locked            True when the account is locked and not allowed to
 *                              log in. Set manually be security officers. Prevents
 *                              login when true.
 * @property  expired           True when the account is expired. Prevents login when true.
 * @property  anonymized        True when this account is anonymized. Prevents log in when true.
 * @property  lastLoginSuccess  Time of the last successful login on this account.
 * @property  loginSuccessCount Number of successful logins on this account (since created).
 * @property  lastLoginFail     Time of last login fail.
 * @property  loginFailCount    Number of failed logins on this account since the last
 *                              successful login.
 */
open class AccountStateExposedTable(
    tableName: String = "account_state"
) : LinkedExposedPaTable(
    tableName
) {

    override val entityId = reference("account", AccountPrivateExposedTableCommon)
    val validated = bool("validated")
    val locked = bool("locked")
    val expired = bool("expired")
    val anonymized = bool("anonymized")
    val lastLoginSuccess = timestamp("last_login_success").nullable()
    val loginSuccessCount = integer("login_success_count")
    val lastLoginFail = timestamp("last_login_fail").nullable()
    val loginFailCount = integer("login_fail_count")

    override val primaryKey by after { PrimaryKey(entityId) }

}
