/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import zakadabar.lib.content.data.ContentCommonBo
import zakadabar.lib.content.data.ContentStereotypeBo
import zakadabar.lib.content.data.ContentTextBo
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.data.entity.EntityId

class ContentCommonExposedPa : ContentCommonExposedPaGen() {

    private val textTable = ContentTextExposedTable

    override fun onModuleLoad() {
        super.onModuleLoad()
        Sql.tables += ContentTextExposedTable
    }

    override fun create(bo: ContentCommonBo): ContentCommonBo {
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

    override fun read(entityId: EntityId<ContentCommonBo>): ContentCommonBo {
        val bo = super.read(entityId)

        bo.textBlocks = textTable
            .select { textTable.content eq bo.id.toLong() }
            .map {
                ContentTextBo(
                    it[textTable.stereotype].entityId(),
                    it[textTable.value]
                )
            }

        return bo
    }

    override fun update(bo: ContentCommonBo): ContentCommonBo {
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

    override fun delete(entityId: EntityId<ContentCommonBo>) {
        super.delete(entityId)
        textTable.deleteWhere { textTable.content eq entityId.toLong() }
    }

    fun byStereotype(stereotypeIds : List<ContentStereotypeBo>) =
        table
            .select { table.stereotype inList stereotypeIds.map { it.id.toLong() }}
            .map { it.toBo() }

}