/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.backend.AccountPrivateExposedTableGen
import zakadabar.lib.content.data.ContentCommonBo
import zakadabar.lib.i18n.backend.LocaleExposedTableGen
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId

/**
 * Exposed based Persistence API for ContentBo.
 * 
 * Generated with Bender at 2021-06-05T06:12:00.491Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class ContentExposedPaGen : ExposedPaBase<ContentCommonBo,ContentCommonExposedTableGen>(
    table = ContentCommonExposedTableGen
) {

    override fun ResultRow.toBo() = ContentCommonBo(
        id = this[table.id].entityId(),
        modifiedAt = this[table.modifiedAt].toKotlinInstant(),
        modifiedBy = this[table.modifiedBy].entityId(),
        status = this[table.status].entityId(),
        category = this[table.category].entityId(),
        master = this[table.master]?.entityId(),
        parent = this[table.parent]?.entityId(),
        locale = this[table.locale]?.entityId(),
        title = this[table.title],
        summary = this[table.summary],
        motto = this[table.motto]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: ContentCommonBo) {
        this[table.modifiedAt] = bo.modifiedAt.toJavaInstant()
        this[table.modifiedBy] = bo.modifiedBy.toLong()
        this[table.status] = bo.status.toLong()
        this[table.category] = bo.category.toLong()
        this[table.master] = bo.master?.let { EntityID(it.toLong(), ContentCommonExposedTableGen) }
        this[table.parent] = bo.parent?.let { EntityID(it.toLong(), ContentCommonExposedTableGen) }
        this[table.locale] = bo.locale?.let { EntityID(it.toLong(), LocaleExposedTableGen) }
        this[table.title] = bo.title
        this[table.summary] = bo.summary
        this[table.motto] = bo.motto
    }
}

/**
 * Exposed based SQL table for ContentBo.
 * 
 * Generated with Bender at 2021-06-05T06:12:00.493Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object ContentCommonExposedTableGen : ExposedPaTable<ContentCommonBo>(
    tableName = "content"
) {

    internal val modifiedAt = timestamp("modified_at")
    internal val modifiedBy = reference("modified_by", AccountPrivateExposedTableGen)
    internal val status = reference("status", ContentStatusExposedTableGen)
    internal val category = reference("category", ContentCategoryExposedTableGen)
    internal val master = reference("master", ContentCommonExposedTableGen).nullable()
    internal val parent = reference("parent", ContentCommonExposedTableGen).nullable()
    internal val locale = reference("locale", LocaleExposedTableGen).nullable()
    internal val title = varchar("title", 100)
    internal val summary = varchar("summary", 1000)
    internal val motto = varchar("motto", 200)

}