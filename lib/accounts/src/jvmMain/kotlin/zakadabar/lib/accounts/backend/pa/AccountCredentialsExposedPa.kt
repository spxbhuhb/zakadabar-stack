/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.backend.pa

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.AccountCredentialsBo
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.backend.exposed.LinkedExposedPaBase
import zakadabar.stack.backend.exposed.LinkedExposedPaTable
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.BCrypt
import zakadabar.stack.util.after

open class AccountCredentialsExposedPa(
    table: AccountCredentialsExposedTable = AccountCredentialsExposedTable()
) : LinkedExposedPaBase<AccountCredentialsBo, AccountPrivateBo, AccountCredentialsExposedTable>(
    table
) {

    override fun linkedId(bo: AccountCredentialsBo) = bo.accountId

    fun read(entityId: EntityId<AccountPrivateBo>, type : String) =
        table
            .select { (table.entityId eq entityId.toLong()) and (table.type eq type) }
            .first()
            .toBo()

    override fun ResultRow.toBo() = AccountCredentialsBo(
        accountId = this[table.entityId].entityId(),
        type = this[table.type],
        value = Secret(this[table.value]),
        expiration = this[table.expiration]?.toKotlinInstant()
    )

    override fun UpdateBuilder<*>.fromBo(bo: AccountCredentialsBo) {
        this[table.entityId] = bo.accountId.toLong()
        this[table.type] = bo.type
        this[table.value] = BCrypt.hashpw(bo.value.value, BCrypt.gensalt())
        this[table.expiration] = bo.expiration?.toJavaInstant()
    }
}

/**
 * Stores account credentials. Each credential has a type that defines the context
 * the given credential is used for, like "password". Default table name is
 * `account_credentials`.
 *
 * @property  entityId    The id of the [AccountPrivateBo] BO this credentials belong to.
 * @property  type        Type of the credential. Value set depends on the
 *                        authentication modules used.
 * @property  value       Value of the credential. Structure of this value
 *                        depends on [type].
 * @property  expiration  Expiration of this credential, if any.
 */
open class AccountCredentialsExposedTable(
    tableName: String = "account_credential"
) : LinkedExposedPaTable(
    tableName
) {

    override val entityId = reference("account", AccountPrivateExposedTableCommon)
    val type = varchar("type", 50)
    val value = text("value")
    val expiration = timestamp("expiration").nullable()

    override val primaryKey by after { PrimaryKey(entityId, type) }

}
