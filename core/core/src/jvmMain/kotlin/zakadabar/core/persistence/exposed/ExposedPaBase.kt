/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.persistence.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.core.persistence.EntityPersistenceApi
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId

/**
 * Persistence API base to be used by Exposed persistence APIs.
 */
abstract class ExposedPaBase<T : EntityBo<T>, TT : ExposedPaTable<T>>(
    open val table: TT
) : EntityPersistenceApi<T> {

    override fun onModuleLoad() {
        super.onModuleLoad()
        if (table !in Sql.tables) Sql.tables += table
    }

    override fun <R> withTransaction(func: () -> R) = transaction {
        func()
    }

    override fun commit() = TransactionManager.current().commit()

    override fun rollback() = TransactionManager.current().commit()

    override fun list() =
        table
            .selectAll()
            .map { it.toBo() }

    override fun createWithId(bo: T) =
        table
            .insert {
                it[id] = bo.id.toLong()
                it.fromBo(bo)
            }
            .let { bo }

    override fun create(bo: T) =
        table
            .insertAndGetId { it.fromBo(bo) }
            .also { bo.id = EntityId(it.value) }
            .let { bo }

    override fun read(entityId: EntityId<T>) =
        table
            .select { table.id eq entityId.toLong() }
            .first()
            .toBo()

    override fun readOrNull(entityId: EntityId<T>) =
        table
            .select { table.id eq entityId.toLong() }
            .firstOrNull()
            ?.toBo()

    override fun update(bo: T) =
        table
            .update({ table.id eq bo.id.toLong() }) { it.fromBo(bo) }
            .let { bo }

    override fun delete(entityId: EntityId<T>) {
        table.deleteWhere { table.id eq entityId.toLong() }
    }

    abstract fun ResultRow.toBo() : T

    abstract fun UpdateBuilder<*>.fromBo(bo: T)

}