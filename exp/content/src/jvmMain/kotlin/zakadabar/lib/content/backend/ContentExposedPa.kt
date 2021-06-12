/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.backend.AccountPrivateExposedTableGen
import zakadabar.lib.content.data.ContentBo
import zakadabar.lib.content.data.StereotypeBo
import zakadabar.lib.content.data.TextBlockBo
import zakadabar.lib.i18n.backend.LocaleExposedTableGen
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.data.entity.EntityId

open class ContentExposedPa : ExposedPaBase<ContentBo, ContentExposedTable>(
    table = ContentExposedTable
) {

    private val textTable = TextBlockExposedTable

    override fun onModuleLoad() {
        super.onModuleLoad()
        Sql.tables += TextBlockExposedTable
    }

    override fun create(bo: ContentBo): ContentBo {
        val created = super.create(bo)

        bo.textBlocks.forEach { textBo ->
            textTable.insert {
                it[content] = bo.id.toLong()
                it[stereotype] = textBo.stereotype.toLong()
                it[value] = textBo.value
            }
        }

        return created
    }

    override fun read(entityId: EntityId<ContentBo>): ContentBo {
        val bo = super.read(entityId)

        bo.textBlocks = textTable
            .select { textTable.content eq bo.id.toLong() }
            .map {
                TextBlockBo(
                    it[textTable.stereotype].entityId(),
                    it[textTable.value]
                )
            }

        return bo
    }

    override fun update(bo: ContentBo): ContentBo {
        super.update(bo)

        textTable.deleteWhere { textTable.content eq bo.id.toLong() }

        bo.textBlocks.forEach { textBo ->
            textTable.insert {
                it[content] = bo.id.toLong()
                it[stereotype] = textBo.stereotype.toLong()
                it[value] = textBo.value
            }
        }

        return bo
    }

    override fun delete(entityId: EntityId<ContentBo>) {
        super.delete(entityId)
        textTable.deleteWhere { textTable.content eq entityId.toLong() }
    }

    fun byStereotype(stereotypeIds: List<StereotypeBo>) =
        table
            .select { table.stereotype inList stereotypeIds.map { it.id.toLong() } }
            .map { it.toBo() }

    fun byLocalizedTitle(stereotypeId: EntityId<StereotypeBo>, localizedContentTitle: String) =
        table
            .select { table.localizedTitle eq localizedContentTitle }
            .andWhere { table.stereotype eq stereotypeId.toLong() }
            .first()
            .toBo()

    override fun ResultRow.toBo() = ContentBo(
        id = this[table.id].entityId(),
        modifiedAt = this[table.modifiedAt].toKotlinInstant(),
        modifiedBy = this[table.modifiedBy].entityId(),
        status = this[table.status].entityId(),
        stereotype = this[table.stereotype].entityId(),
        master = this[table.master]?.entityId(),
        position = this[table.position],
        locale = this[table.locale]?.entityId(),
        title = this[table.title],
        localizedTitle = this[table.localizedTitle],
        summary = this[table.summary],
        textBlocks = emptyList()
    )

    override fun UpdateBuilder<*>.fromBo(bo: ContentBo) {
        this[table.modifiedAt] = bo.modifiedAt.toJavaInstant()
        this[table.modifiedBy] = bo.modifiedBy.toLong()
        this[table.status] = bo.status.toLong()
        this[table.stereotype] = bo.stereotype.toLong()
        this[table.master] = bo.master?.let { EntityID(it.toLong(), ContentExposedTable) }
        this[table.position] = bo.position
        this[table.locale] = bo.locale?.let { EntityID(it.toLong(), LocaleExposedTableGen) }
        this[table.title] = bo.title
        this[table.localizedTitle] = bo.localizedTitle
        this[table.summary] = bo.summary
    }
}

object ContentExposedTable : ExposedPaTable<ContentBo>(
    tableName = "content"
) {

    internal val modifiedAt = timestamp("modified_at")
    internal val modifiedBy = reference("modified_by", AccountPrivateExposedTableGen)
    internal val status = reference("status", StatusExposedTableGen)
    internal val stereotype = reference("stereotype", StereotypeExposedTableGen)
    internal val master = reference("master", ContentExposedTable).nullable()
    internal val position = integer("position")
    internal val locale = reference("locale", LocaleExposedTableGen).nullable()
    internal val title = varchar("title", 100)
    internal val localizedTitle = varchar("title", 100).index()
    internal val summary = varchar("summary", 1000)

}