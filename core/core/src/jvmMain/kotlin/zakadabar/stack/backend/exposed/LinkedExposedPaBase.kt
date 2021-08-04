/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId

/**
 * Persistence API base to be used by Exposed persistence APIs that do
 * not handle entities but data linked to entities.
 */
abstract class LinkedExposedPaBase<T : BaseBo, ET : EntityBo<ET>, TT : LinkedExposedPaTable>(
    val table: TT
) {

    open fun onModuleLoad() {
        if (table !in Sql.tables) Sql.tables += table
    }

    open fun list() =
        table
            .selectAll()
            .map { it.toBo() }

    open fun create(bo: T) =
        table
            .insert { it.fromBo(bo) }
            .let { bo }

    open fun read(entityId: EntityId<ET>) =
        table
            .select { table.entityId eq entityId.toLong() }
            .first()
            .toBo()

    open fun readOrNull(entityId: EntityId<ET>) =
        table
            .select { table.entityId eq entityId.toLong() }
            .firstOrNull()
            ?.toBo()

    open fun update(bo: T) =
        table
            .update({ table.entityId eq linkedId(bo).toLong() }) { it.fromBo(bo) }
            .let { bo }

    open fun delete(entityId: EntityId<ET>) {
        table.deleteWhere { table.entityId eq entityId.toLong() }
    }

    abstract fun linkedId(bo : T) : EntityId<ET>

    abstract fun ResultRow.toBo() : T

    abstract fun UpdateBuilder<*>.fromBo(bo: T)

}