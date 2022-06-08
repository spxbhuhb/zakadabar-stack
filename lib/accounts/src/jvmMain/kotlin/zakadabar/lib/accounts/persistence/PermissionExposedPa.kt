/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.persistence

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.business.AccountPrivateBl
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.Sql
import zakadabar.core.persistence.exposed.entityId
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.lib.accounts.data.PermissionBo
import zakadabar.lib.accounts.data.RoleBo
import zakadabar.lib.accounts.data.RoleGrantBo
import zakadabar.lib.accounts.data.RolePermissionBo

open class PermissionExposedPa(
    open val roleTable: RoleExposedTable = RoleExposedTableCommon,
    open val rolePermissionTable: RolePermissionExposedTable = RolePermissionExposedTableCommon
): ExposedPaBase<PermissionBo, PermissionExposedTable>(
    table = PermissionExposedTableCommon
) {

    override fun onModuleLoad() {
        super.onModuleLoad()
        Sql.tables += table
        Sql.tables += rolePermissionTable
    }

    fun permissionsOf(accountId: EntityId<AccountPrivateBl>) =
        table
            .join(
                joinType = JoinType.INNER,
                otherTable = rolePermissionTable,
                onColumn = table.id,
                otherColumn = rolePermissionTable.permission,
            )
            .join(
                joinType = JoinType.INNER,
                otherTable = RoleExposedTableCommon,
                onColumn = rolePermissionTable.role,
                otherColumn = RoleExposedTableCommon.id,
            )
            .join(
                joinType = JoinType.INNER,
                otherTable = RoleGrantExposedTableCommon,
                onColumn = RoleExposedTableCommon.id,
                otherColumn = RoleGrantExposedTableCommon.role,
            )
            .slice(table.name, table.id)
            .select { RoleGrantExposedTableCommon.account eq accountId.toLong() }
            .map {
                EntityId<BaseBo>(it[table.id].value) to it[table.name]
            }

    fun byRole(role: EntityId<RoleBo>) =
        table
            .join(
                joinType = JoinType.INNER,
                otherTable = rolePermissionTable,
                onColumn = table.id,
                otherColumn = rolePermissionTable.permission,
            )
            .slice(rolePermissionTable.role, rolePermissionTable.permission)
            .select { rolePermissionTable.role eq role.toLong() }
            .map {
                RolePermissionBo(
                    permission = it[rolePermissionTable.permission].entityId(),
                    role = it[rolePermissionTable.role].entityId()
                )
            }

    fun addPermission(rolePermission: RolePermissionBo) {
        // TODO Exposed: insertIgnore would be better but not supported out-of-the box
        val exists = rolePermissionTable
            .select {
                (rolePermissionTable.permission eq rolePermission.permission.toLong()) and
                        (rolePermissionTable.role eq rolePermission.role.toLong())
            }
            .count() != 0L

        if (exists) return

        rolePermissionTable.insert {
            it[permission] = EntityID(rolePermission.permission.toLong(), table)
            it[role] = EntityID(rolePermission.role.toLong(), roleTable)
        }
    }

    fun removePermission(rolePermission: RolePermissionBo) {
        rolePermissionTable.deleteWhere {
            (rolePermissionTable.permission eq rolePermission.permission.toLong()) and
                    (rolePermissionTable.role eq rolePermission.role.toLong())
        }
    }

    override fun ResultRow.toBo(): PermissionBo {
        return PermissionBo(
            id = this[table.id].entityId(),
            name = this[table.name],
            description = this[table.description]
        )
    }

    override fun UpdateBuilder<*>.fromBo(bo: PermissionBo) {
        this[table.name] = bo.name
        this[table.description] = bo.description
    }
}

object PermissionExposedTableCommon : PermissionExposedTable()

open class PermissionExposedTable : ExposedPaTable<PermissionBo>(
    tableName = "permission"
) {
    val name = varchar("name", 50).index()
    val description = text("description")
}

object RolePermissionExposedTableCommon : RolePermissionExposedTable()

open class RolePermissionExposedTable: Table("role_permission") {

    val role = reference("role", RoleExposedTableCommon)
    val permission = reference("permission", PermissionExposedTableCommon)

    override val primaryKey = PrimaryKey(role, permission)

}
