/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.data.EntityId
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.Sql
import zakadabar.core.persistence.exposed.entityId
import zakadabar.lib.content.data.*
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.persistence.LocaleExposedTableGen

open class ContentPa : ExposedPaBase<ContentBo, ContentTable>(
    table = ContentTable
) {

    private val textTable = TextBlockTable

    override fun onModuleLoad() {
        super.onModuleLoad()
        Sql.tables += textTable
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

        readTextBlocks(bo)

        return bo
    }

    private fun readTextBlocks(bo: ContentBo) {
        bo.textBlocks = textTable
            .select { textTable.content eq bo.id.toLong() }
            .map {
                TextBlockBo(
                    it[textTable.stereotype],
                    it[textTable.value]
                )
            }
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
        val bo = table
            .innerJoin(jt, { table.master }, { jt[table.id] })
            .select { table.seoTitle eq seoTitle }
            .andWhere { jt[table.parent] eq parent?.toLong() }
            .andWhere { table.locale eq locale.toLong() }
            .firstOrNull()
            ?.toBo()
            ?: return null

        readTextBlocks(bo)

        return bo
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
        status = this[table.status].entityId(),
        folder = this[table.folder],
        parent = this[table.parent]?.entityId(),
        master = this[table.master]?.entityId(),
        position = this[table.position],
        locale = this[table.locale]?.entityId(),
        title = this[table.title],
        seoTitle = this[table.seoTitle],
        textBlocks = emptyList(),
        attachments = emptyList()
    )

    override fun UpdateBuilder<*>.fromBo(bo: ContentBo) {
        this[table.status] = bo.status.toLong()
        this[table.folder] = bo.folder
        this[table.parent] = bo.parent?.let { EntityID(it.toLong(), table) }
        this[table.master] = bo.master?.let { EntityID(it.toLong(), table) }
        this[table.position] = bo.position
        this[table.locale] = bo.locale?.let { EntityID(it.toLong(), LocaleExposedTableGen) }
        this[table.title] = bo.title
        this[table.seoTitle] = bo.seoTitle
    }

}

