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
import zakadabar.lib.accounts.backend.AccountPrivateExposedTable
import zakadabar.lib.content.data.*
import zakadabar.lib.i18n.backend.LocaleExposedTableGen
import zakadabar.lib.i18n.data.LocaleBo
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
                it[stereotype] = textBo.stereotype
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
                    it[textTable.stereotype],
                    it[textTable.value]
                )
            }

        return bo
    }

    fun readLocalized(master: EntityId<ContentBo>, locale: EntityId<LocaleBo>) =
        readLocalizedOrNull(master, locale) ?: throw NoSuchElementException("masterId=$master localeId=$locale")

    fun readLocalizedOrNull(master: EntityId<ContentBo>, locale: EntityId<LocaleBo>): ContentBo? {
        val bo = table
            .select { (table.master eq master.toLong()) and (table.locale eq locale.toLong()) }
            .firstOrNull()
            ?.toBo()
            ?: return null

        bo.textBlocks = textTable
            .select { textTable.content eq bo.id.toLong() }
            .map {
                TextBlockBo(
                    it[textTable.stereotype],
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
                it[stereotype] = textBo.stereotype
                it[value] = textBo.value
            }
        }

        return bo
    }

    override fun delete(entityId: EntityId<ContentBo>) {
        super.delete(entityId)
        textTable.deleteWhere { textTable.content eq entityId.toLong() }
    }

    fun bySeoTitle(locale: EntityId<LocaleBo>, parent: EntityId<ContentBo>?, seoTitle: String): ContentBo? {
        val jt = table.alias("jt")
        return table
            .innerJoin(jt, { table.master }, { jt[table.id] })
            .select { table.seoTitle eq seoTitle }
            .andWhere { jt[table.parent] eq parent?.toLong() }
            .andWhere { table.locale eq locale.toLong() }
            .firstOrNull()
            ?.toBo()
    }

    fun mastersQuery(): List<MastersEntry> =
        table
            .slice(table.id, table.title)
            .select { table.master.isNull() }
            .map {
                MastersEntry(
                    it[table.id].entityId(),
                    it[table.title]
                )
            }

    fun folderQuery(): List<FolderEntry> =
        table
            .slice(table.id, table.title)
            .select { table.master.isNull() and (table.folder eq true) }
            .map {
                FolderEntry(
                    it[table.id].entityId(),
                    it[table.title]
                )
            }

    fun navQuery(localeId: EntityId<LocaleBo>, parentMasterId: EntityId<ContentBo>?, parentSeoPath: String): List<NavEntry> =
        localizedChildrenNav(localeId, parentMasterId, parentSeoPath).onEach { entry ->
            if (entry.folder) {
                entry.children = navQuery(localeId, entry.masterId, entry.seoPath)
            }
        }

    private fun localizedChildrenNav(locale: EntityId<LocaleBo>, parentMasterId: EntityId<ContentBo>?, parentSeoPath: String): List<NavEntry> {
        val jt = table.alias("jt")
        return table
            .innerJoin(jt, { table.master }, { jt[table.id] })
            .slice(table.id, table.title, table.seoTitle, jt[table.id], jt[table.folder])
            .select { jt[table.parent] eq parentMasterId?.toLong() }
            .andWhere { table.locale eq locale.toLong() }
            .map {
                NavEntry(
                    it[jt[table.id]].entityId(),
                    it[table.id].entityId(),
                    it[table.title],
                    "${parentSeoPath}/${it[table.seoTitle]}",
                    it[jt[table.folder]],
                    emptyList()
                )
            }
    }

    fun thumbnailQuery(localeId: EntityId<LocaleBo>, parentMasterId: EntityId<ContentBo>?, parentSeoPath: String): List<ThumbnailEntry> {
        val jt = table.alias("jt")

        return table
            .innerJoin(jt, { table.master }, { jt[table.id] })
            .slice(table.id, table.title, table.seoTitle, jt[table.id])
            .select { jt[table.parent] eq parentMasterId?.toLong() }
            .andWhere { table.locale eq localeId.toLong() }
            .map {
                ThumbnailEntry(
                    it[jt[table.id]].entityId(),
                    it[table.id].entityId(),
                    it[table.title],
                    "${parentSeoPath}/${it[table.seoTitle]}",
                    "" // this will be filled later
                )
            }
    }

    override fun ResultRow.toBo() = ContentBo(
        id = this[table.id].entityId(),
        modifiedAt = this[table.modifiedAt].toKotlinInstant(),
        modifiedBy = this[table.modifiedBy].entityId(),
        status = this[table.status].entityId(),
        folder = this[table.folder],
        parent = this[table.parent]?.entityId(),
        master = this[table.master]?.entityId(),
        position = this[table.position],
        locale = this[table.locale]?.entityId(),
        title = this[table.title],
        seoTitle = this[table.seoTitle],
        textBlocks = emptyList()
    )

    override fun UpdateBuilder<*>.fromBo(bo: ContentBo) {
        this[table.modifiedAt] = bo.modifiedAt.toJavaInstant()
        this[table.modifiedBy] = bo.modifiedBy.toLong()
        this[table.status] = bo.status.toLong()
        this[table.folder] = bo.folder
        this[table.parent] = bo.parent?.let { EntityID(it.toLong(), ContentExposedTable) }
        this[table.master] = bo.master?.let { EntityID(it.toLong(), ContentExposedTable) }
        this[table.position] = bo.position
        this[table.locale] = bo.locale?.let { EntityID(it.toLong(), LocaleExposedTableGen) }
        this[table.title] = bo.title
        this[table.seoTitle] = bo.seoTitle
    }

}

object ContentExposedTable : ExposedPaTable<ContentBo>(
    tableName = "content"
) {

    val modifiedAt = timestamp("modified_at")
    val modifiedBy = reference("modified_by", AccountPrivateExposedTable)
    val status = reference("status", StatusExposedTableGen)
    val folder = bool("folder")
    val parent = reference("parent", ContentExposedTable).nullable()
    val master = reference("master", ContentExposedTable).nullable()
    val position = long("position")
    val locale = reference("locale", LocaleExposedTableGen).nullable()
    val title = varchar("title", 100)
    val seoTitle = varchar("seo_title", 100).index()

}

object TextBlockExposedTable : Table("content_text") {

    val content = reference("content", ContentExposedTable).index()
    val stereotype = varchar("stereotype", 100)
    val value = text("value")

}