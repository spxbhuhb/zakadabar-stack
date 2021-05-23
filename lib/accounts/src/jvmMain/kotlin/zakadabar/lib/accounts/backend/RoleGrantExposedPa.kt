/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.stack.backend.data.builtin.principal.PrincipalTable
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantTable
import zakadabar.stack.backend.data.entity.EntityPersistenceApiBase
import zakadabar.stack.backend.data.entity.ExposedPersistenceApi
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.data.builtin.account.RoleGrantBo
import zakadabar.stack.data.entity.EntityId

class RoleGrantExposedPa : EntityPersistenceApiBase<RoleGrantBo>(), ExposedPersistenceApi {

    override fun onModuleLoad() {
        super.onModuleLoad()
        + RoleGrantExposedTable
    }

    override fun list(): List<RoleGrantBo> {
        return RoleGrantExposedTable
            .selectAll()
            .map { RoleGrantExposedTable.toBo(it) }
    }

    override fun create(bo: RoleGrantBo): RoleGrantBo {
        val id = RoleGrantExposedTable
            .insertAndGetId {
                it[id] = bo.id.toLong()
                fromBo(it, bo)
            }
        bo.id = EntityId(id.value)
        return bo
    }

    override fun read(entityId: EntityId<RoleGrantBo>) : RoleGrantBo {
        return RoleGrantExposedTable
            .select { RoleGrantExposedTable.id eq entityId.toLong() }
            .first()
            .let { RoleGrantExposedTable.toBo(it) }
    }

    override fun update(bo: RoleGrantBo): RoleGrantBo {
        RoleGrantExposedTable
            .update({ RoleGrantExposedTable.id eq bo.id.toLong() })
            { fromBo(it, bo) }
        return bo
    }

    override fun delete(entityId: EntityId<RoleGrantBo>) {
        RoleGrantExposedTable
            .deleteWhere { RoleGrantExposedTable.id eq entityId.toLong() }
    }

}

object RoleGrantExposedTable : LongIdTable("role_grants") {

    val principal = RoleGrantTable.reference("principal", PrincipalTable).index()
    val role = RoleGrantTable.reference("role", zakadabar.stack.backend.data.builtin.role.RoleTable).index()

    fun toBo(row: ResultRow) = RoleGrantBo(
        id = row[RoleGrantTable.id].entityId(),
        principal = row[principal].entityId(),
        role = row[role].entityId()
    )

    fun fromBo(statement: UpdateBuilder<*>, bo: RoleGrantBo) {
        statement[principal] = bo.principal.toLong()
        statement[role] = bo.role.toLong()
    }

}