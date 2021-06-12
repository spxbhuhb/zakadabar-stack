/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.content.data.StereotypeBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId

open class StereotypeExposedPaGen : ExposedPaBase<StereotypeBo,StereotypeExposedTableGen>(
    table = StereotypeExposedTableGen
) {
    override fun ResultRow.toBo() = StereotypeBo(
        id = this[table.id].entityId(),
        parent = this[table.parent]?.entityId(),
        name = this[table.name],
        localizations = emptyList()
    )  

    override fun UpdateBuilder<*>.fromBo(bo: StereotypeBo) {
        this[table.parent] = bo.parent?.let { EntityID(it.toLong(), StereotypeExposedTableGen) }
        this[table.name] = bo.name
    }

}

/**
 * Exposed based SQL table for StereotypeBo.
 * 
 * Generated with Bender at 2021-06-10T04:08:05.312Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object StereotypeExposedTableGen : ExposedPaTable<StereotypeBo>(
    tableName = "content_stereotype"
) {

    internal val parent = reference("parent", StereotypeExposedTableGen).nullable()
    internal val name = varchar("key", 100)

}