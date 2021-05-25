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

    val table = RoleExposedTable()

    override fun onModuleLoad() {
        super.onModuleLoad()
        + table
    }

    override fun <R> withTransaction(func: () -> R) = transaction {
        func()
    }

    override fun commit() = exposedCommit()

    override fun rollback() = exposedRollback()

    override fun list(): List<RoleBo> {
        return table.selectAll()
            .map { it.toBo() }
    }

    override fun create(bo: RoleBo): RoleBo {
        table
            .insertAndGetId { it.fromBo(bo) }
            .also { bo.id = EntityId(it.value) }
        return bo
    }

    override fun read(entityId: EntityId<RoleBo>): RoleBo {
        return table
            .select { table.id eq entityId.toLong() }
            .first()
            .toBo()
    }

    override fun update(bo: RoleBo): RoleBo {
        table
            .update({ table.id eq bo.id.toLong() }) { it.fromBo(bo) }
        return bo
    }

    override fun delete(entityId: EntityId<RoleBo>) {
        table
            .deleteWhere { table.id eq entityId.toLong() }
    }

    open fun ResultRow.toBo() = table.toBo(this)

    open fun UpdateBuilder<*>.fromBo(bo: RoleBo) = table.fromBo(this, bo)

}

class RoleExposedTable : LongIdTable("roles") {

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

