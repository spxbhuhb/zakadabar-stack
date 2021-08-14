/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.backend.bl.AccountPrivateBl
import zakadabar.lib.accounts.backend.pa.AccountPrivateExposedTable
import zakadabar.lib.accounts.backend.pa.AccountPrivateExposedTableCommon
import zakadabar.lib.accounts.data.RoleBo
import zakadabar.lib.accounts.data.RoleGrantBo
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.Sql
import zakadabar.core.persistence.exposed.entityId
import zakadabar.core.data.BaseBo
import zakadabar.core.data.builtin.account.AccountPublicBo
import zakadabar.core.data.entity.EntityId

open class RoleExposedPa(
    open val accountTable: AccountPrivateExposedTable = AccountPrivateExposedTableCommon,
    open val grantTable: RoleGrantExposedTable = RoleGrantExposedTableCommon
) : ExposedPaBase<RoleBo, RoleExposedTable>(
    table = RoleExposedTableCommon
) {

    override fun onModuleLoad() {
        super.onModuleLoad()
        Sql.tables += table
        Sql.tables += grantTable
    }

    fun readByName(name: String) = table.select { table.name eq name }.first().toBo()

    fun rolesOf(accountId: EntityId<AccountPrivateBl>) =
        grantTable
            .join(table, JoinType.INNER, additionalConstraint = { table.id eq grantTable.role })
            .slice(table.name, table.id)
            .select { grantTable.account eq accountId.toLong() }
            .map {
                EntityId<BaseBo>(it[table.id].value) to it[table.name]
            }

    fun rolesByAccount(accountId: EntityId<AccountPublicBo>) =
        grantTable
            .select { grantTable.account eq accountId.toLong() }
            .map {
                RoleGrantBo(
                    EntityId(it[grantTable.account].value),
                    EntityId(it[grantTable.role].value)
                )
            }


    fun accountsByRole(roleId: EntityId<RoleBo>, withEmail : Boolean, withPhone : Boolean): List<AccountPublicBo> {
        return grantTable
            .innerJoin(accountTable, { grantTable.account }, { accountTable.id })
            .slice(
                accountTable.id,
                accountTable.accountName,
                accountTable.fullName,
                accountTable.email,
                accountTable.theme,
                accountTable.locale
            )
            .select { grantTable.role eq roleId.toLong() }
            .map {
                AccountPublicBo(
                    EntityId(it[accountTable.id].value),
                    accountName = it[accountTable.accountName],
                    fullName = it[accountTable.fullName],
                    email = if (withEmail) it[accountTable.email] else null,
                    phone = if (withPhone) it[accountTable.phone] else null,
                    theme = it[accountTable.theme],
                    locale = it[accountTable.locale]
                )
            }
    }


    fun grant(grant: RoleGrantBo) {
        // TODO Exposed: insertIgnore would be better but not supported out-of-the box
        val exists = grantTable
            .select { (grantTable.account eq grant.account.toLong()) and (grantTable.role eq grant.role.toLong()) }
            .count() != 0L

        if (exists) return

        grantTable.insert {
            it[account] = EntityID(grant.account.toLong(), accountTable)
            it[role] = EntityID(grant.role.toLong(), table)
        }
    }

    fun revoke(revoke: RoleGrantBo) {
        grantTable.deleteWhere {
            (grantTable.account eq revoke.account.toLong()) and (grantTable.role eq revoke.role.toLong())
        }
    }

    override fun ResultRow.toBo(): RoleBo {
        return RoleBo(
            id = this[table.id].entityId(),
            name = this[table.name],
            description = this[table.description]
        )
    }

    override fun UpdateBuilder<*>.fromBo(bo: RoleBo) {
        this[table.name] = bo.name
        this[table.description] = bo.description
    }
}

object RoleExposedTableCommon : RoleExposedTable()

open class RoleExposedTable : ExposedPaTable<RoleBo>(
    tableName = "role"
) {
    val name = varchar("name", 50).index()
    val description = text("description")
}

object RoleGrantExposedTableCommon : RoleGrantExposedTable(AccountPrivateExposedTableCommon)

open class RoleGrantExposedTable(
    accountTable: AccountPrivateExposedTable
) : Table("role_grant") {

    val account = reference("account", accountTable)
    val role = reference("role", RoleExposedTableCommon)

    override val primaryKey = PrimaryKey(account, role)

}
