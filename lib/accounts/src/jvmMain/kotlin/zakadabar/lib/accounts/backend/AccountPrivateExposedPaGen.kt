/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.backend.data.entity.EntityPersistenceApi
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.exposed.ExposedPersistenceApi
import zakadabar.stack.data.entity.EntityId


/*
 * Exposed based Persistence API for AccountPrivateBo.
 * 
 * Generated with Bender at 2021-05-24T06:11:54.844Z.
 *
 * IMPORTANT: Please do not modify this file, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class AccountPrivateExposedPaGen : EntityPersistenceApi<AccountPrivateBo>, ExposedPersistenceApi {

    override fun onModuleLoad() {
        super.onModuleLoad()
        + AccountPrivateExposedTable
    }
    
    override fun <R> withTransaction(func : () -> R) = transaction {
        func()
    }

    override fun list(): List<AccountPrivateBo> {
        return AccountPrivateExposedTable
            .selectAll()
            .map { it.toBo() }
    }

    override fun create(bo: AccountPrivateBo): AccountPrivateBo {
        AccountPrivateExposedTable
            .insertAndGetId { it.fromBo(bo) }
            .also { bo.id = EntityId(it.value) }
        return bo
    }

    override fun read(entityId: EntityId<AccountPrivateBo>) : AccountPrivateBo {
        return AccountPrivateExposedTable
            .select { AccountPrivateExposedTable.id eq entityId.toLong() }
            .first()
            .toBo()
    }

    override fun update(bo: AccountPrivateBo): AccountPrivateBo {
        AccountPrivateExposedTable
            .update({ AccountPrivateExposedTable.id eq bo.id.toLong() }) { it.fromBo(bo) }
        return bo
    }

    override fun delete(entityId: EntityId<AccountPrivateBo>) {
        AccountPrivateExposedTable
            .deleteWhere { AccountPrivateExposedTable.id eq entityId.toLong() }
    }
    
    open fun ResultRow.toBo() = AccountPrivateExposedTable.toBo(this)

    open fun UpdateBuilder<*>.fromBo(bo : AccountPrivateBo) = AccountPrivateExposedTable.fromBo(this, bo)

}

object AccountPrivateExposedTable : LongIdTable("account_private_bo") {

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

    fun toBo(row: ResultRow) = AccountPrivateBo(
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

    fun fromBo(statement: UpdateBuilder<*>, bo: AccountPrivateBo) {
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

