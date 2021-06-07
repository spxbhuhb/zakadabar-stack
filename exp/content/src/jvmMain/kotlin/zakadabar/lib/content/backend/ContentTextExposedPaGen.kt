/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.content.data.ContentTextBo
import zakadabar.lib.content.data.ContentTextType
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId


/**
 * Exposed based Persistence API for ContentTextBo.
 * 
 * Generated with Bender at 2021-06-07T02:56:36.423Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class ContentTextExposedPaGen : ExposedPaBase<ContentTextBo,ContentTextExposedTableGen>(
    table = ContentTextExposedTableGen
) {
    override fun ResultRow.toBo() = ContentTextBo(
        id = this[table.id].entityId(),
        content = this[table.content].entityId(),
        type = this[table.type],
        value = this[table.value]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: ContentTextBo) {
        this[table.content] = bo.content.toLong()
        this[table.type] = bo.type
        this[table.value] = bo.value
    }
}

/**
 * Exposed based SQL table for ContentTextBo.
 * 
 * Generated with Bender at 2021-06-07T02:56:36.424Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object ContentTextExposedTableGen : ExposedPaTable<ContentTextBo>(
    tableName = "content_text"
) {

    internal val content = reference("content", ContentCommonExposedTableGen)
    internal val type = enumerationByName("type", 20, ContentTextType::class) // TODO check size of ContentTextType in the exposed table
    internal val value = text("value")

}