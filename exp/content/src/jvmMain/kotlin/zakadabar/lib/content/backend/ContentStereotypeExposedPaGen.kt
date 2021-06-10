/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.content.data.ContentStereotypeBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId

/**
 * Exposed based Persistence API for ContentStereotypeBo.
 * 
 * Generated with Bender at 2021-06-10T04:08:05.311Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class ContentStereotypeExposedPaGen : ExposedPaBase<ContentStereotypeBo,ContentStereotypeExposedTableGen>(
    table = ContentStereotypeExposedTableGen
) {
    override fun ResultRow.toBo() = ContentStereotypeBo(
        id = this[table.id].entityId(),
        parent = this[table.parent]?.entityId(),
        key = this[table.key]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: ContentStereotypeBo) {
        this[table.parent] = bo.parent?.let { EntityID(it.toLong(), ContentStereotypeExposedTableGen) }
        this[table.key] = bo.key
    }
}

/**
 * Exposed based SQL table for ContentStereotypeBo.
 * 
 * Generated with Bender at 2021-06-10T04:08:05.312Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object ContentStereotypeExposedTableGen : ExposedPaTable<ContentStereotypeBo>(
    tableName = "content_stereotype"
) {

    internal val parent = reference("parent", ContentStereotypeExposedTableGen).nullable()
    internal val key = varchar("key", 100)

}