/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.accounts.data.RoleBo
import zakadabar.stack.backend.data.entity.EntityPersistenceApi
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.exposed.ExposedPersistenceApi
import zakadabar.stack.data.entity.EntityId


open class RoleExposedPaGen : EntityPersistenceApi<RoleBo>, ExposedPersistenceApi {

    override fun onModuleLoad() {
        super.onModuleLoad()
        + RoleExposedTable
    }

    override fun <R> withTransaction(func: () -> R) = transaction {
        func()
    }

    override fun list(): List<RoleBo> {
        return RoleExposedTable
            .selectAll()
            .map { it.toBo() }
    }

    override fun create(bo: RoleBo): RoleBo {
        RoleExposedTable
            .insertAndGetId { it.fromBo(bo) }
            .also { bo.id = EntityId(it.value) }
        return bo
    }

    override fun read(entityId: EntityId<RoleBo>): RoleBo {
        return RoleExposedTable
            .select { RoleExposedTable.id eq entityId.toLong() }
            .first()
            .toBo()
    }

    override fun update(bo: RoleBo): RoleBo {
        RoleExposedTable
            .update({ RoleExposedTable.id eq bo.id.toLong() }) { it.fromBo(bo) }
        return bo
    }

    override fun delete(entityId: EntityId<RoleBo>) {
        RoleExposedTable
            .deleteWhere { RoleExposedTable.id eq entityId.toLong() }
    }

    open fun ResultRow.toBo() = RoleExposedTable.toBo(this)

    open fun UpdateBuilder<*>.fromBo(bo: RoleBo) = RoleExposedTable.fromBo(this, bo)

}

object RoleExposedTable : LongIdTable("role_bo") {

    val name = varchar("name", 50)
    val description = text("description")

    fun toBo(row: ResultRow) = RoleBo(
        id = row[id].entityId(),
        name = row[name],
        description = row[description]
    )

    fun fromBo(statement: UpdateBuilder<*>, bo: RoleBo) {
        statement[name] = bo.name
        statement[description] = bo.description
    }

}

