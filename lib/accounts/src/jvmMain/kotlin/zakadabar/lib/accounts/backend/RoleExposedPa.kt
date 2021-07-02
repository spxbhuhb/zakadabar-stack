/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.ModuleSettings
import zakadabar.lib.accounts.data.RoleBo
import zakadabar.lib.accounts.data.RoleGrantBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.backend.setting.setting
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.entity.EntityId

open class RoleExposedPa(
    open val accountTable: AccountPrivateExposedTable = AccountPrivateExposedTableCommon,
    open val grantTable: RoleGrantExposedTable = RoleGrantExposedTableCommon
) : ExposedPaBase<RoleBo, RoleExposedTable>(
    table = RoleExposedTableCommon
) {

    private val settings by setting<ModuleSettings>()

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


    fun accountsByRole(roleId: EntityId<RoleBo>): List<AccountPublicBo> {
        return grantTable
            .innerJoin(accountTable, { grantTable.account }, { accountTable.id })
            .select { grantTable.role eq roleId.toLong() }
            .map {
                AccountPublicBo(
                    EntityId(it[accountTable.id].value),
                    accountName = it[accountTable.accountName],
                    fullName = it[accountTable.fullName],
                    email = if (settings.emailInAccountPublic) it[accountTable.email] else "",
                    displayName = it[accountTable.displayName],
                    organizationName = "", // FIXME this is not stored yet
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
