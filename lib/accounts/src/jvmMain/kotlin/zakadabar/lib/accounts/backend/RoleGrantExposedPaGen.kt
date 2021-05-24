/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.accounts.data.RoleGrantBo
import zakadabar.stack.backend.data.Sql
import zakadabar.stack.backend.data.entity.EntityPersistenceApi
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.exposed.ExposedPersistenceApi
import zakadabar.stack.data.entity.EntityId


open class RoleGrantExposedPaGen : EntityPersistenceApi<RoleGrantBo>, ExposedPersistenceApi {

    override fun onModuleLoad() {
        super.onModuleLoad()
        Sql.tables += RoleGrantExposedTable
    }

    override fun <R> withTransaction(func: () -> R) = transaction {
        func()
    }

    override fun commit() = exposedCommit()

    override fun rollback() = exposedRollback()

    override fun list(): List<RoleGrantBo> {
        return RoleGrantExposedTable
            .selectAll()
            .map { it.toBo() }
    }

    override fun create(bo: RoleGrantBo): RoleGrantBo {
        RoleGrantExposedTable
            .insertAndGetId { it.fromBo(bo) }
            .also { bo.id = EntityId(it.value) }
        return bo
    }

    override fun read(entityId: EntityId<RoleGrantBo>): RoleGrantBo {
        return RoleGrantExposedTable
            .select { RoleGrantExposedTable.id eq entityId.toLong() }
            .first()
            .toBo()
    }

    override fun update(bo: RoleGrantBo): RoleGrantBo {
        RoleGrantExposedTable
            .update({ RoleGrantExposedTable.id eq bo.id.toLong() }) { it.fromBo(bo) }
        return bo
    }

    override fun delete(entityId: EntityId<RoleGrantBo>) {
        RoleGrantExposedTable
            .deleteWhere { RoleGrantExposedTable.id eq entityId.toLong() }
    }

    open fun ResultRow.toBo() = RoleGrantExposedTable.toBo(this)

    open fun UpdateBuilder<*>.fromBo(bo: RoleGrantBo) = RoleGrantExposedTable.fromBo(this, bo)

}

object RoleGrantExposedTable : LongIdTable("role_grant_bo") {

    val principal = reference("principal", PrincipalExposedTable)
    val role = reference("role", RoleExposedTable)

    fun toBo(row: ResultRow) = RoleGrantBo(
        id = row[id].entityId(),
        principal = row[principal].entityId(),
        role = row[role].entityId()
    )

    fun fromBo(statement: UpdateBuilder<*>, bo: RoleGrantBo) {
        statement[principal] = bo.principal.toLong()
        statement[role] = bo.role.toLong()
    }

}

