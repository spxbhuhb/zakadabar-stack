/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.lib.accounts.data.RoleGrantBo
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

class RoleExposedPa : RoleExposedPaGen() {

    override fun onModuleLoad() {
        super.onModuleLoad()
        Sql.tables += RoleGrantExposedTable
    }

    fun readByName(name: String) = table.select { RoleExposedTableGen.name eq name }.first().toBo()

    fun rolesOf(accountId: EntityId<AccountPrivateBl>) =
        RoleGrantExposedTable
            .join(table, JoinType.INNER, additionalConstraint = { table.id eq RoleGrantExposedTable.role })
            .slice(table.name, table.id)
            .select { RoleGrantExposedTable.account eq accountId.toLong() }
            .map {
                EntityId<BaseBo>(it[table.id].value) to it[table.name]
            }

    fun rolesByAccount(accountId: EntityId<AccountPrivateBo>) =
        RoleGrantExposedTable
            .select { RoleGrantExposedTable.account eq accountId.toLong() }
            .map {
                RoleGrantBo(
                    EntityId(it[RoleGrantExposedTable.account].value),
                    EntityId(it[RoleGrantExposedTable.role].value)
                )
            }

    fun grant(grant: RoleGrantBo) {
        val grants = RoleGrantExposedTable

        // TODO Exposed: insertIgnore would be better but not supported out-of-the box
        val exists = grants
            .select {  (grants.account eq grant.account.toLong()) and (grants.role eq grant.role.toLong())}
            .count() != 0L

        if (exists) return

        grants.insert {
            it[account] = EntityID(grant.account.toLong(), AccountPrivateExposedTableGen)
            it[role] = EntityID(grant.role.toLong(), RoleExposedTableGen)
        }
    }

    fun revoke(revoke: RoleGrantBo) {
        val grants = RoleGrantExposedTable
        grants.deleteWhere {
            (grants.account eq revoke.account.toLong()) and (grants.role eq revoke.role.toLong())
        }
    }

}

object RoleGrantExposedTable : Table("role_grant") {

    internal val account = reference("account", AccountPrivateExposedTableGen)
    internal val role = reference("role", RoleExposedTableGen)

    override val primaryKey = PrimaryKey(account, role)

}
