/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import zakadabar.lib.content.data.StereotypeBo
import zakadabar.lib.content.data.StereotypeLocalizationBo
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.data.entity.EntityId

class StereotypeExposedPa : StereotypeExposedPaGen() {

    private val localizationTable = StereotypeLocalizationTable

    override fun onModuleLoad() {
        super.onModuleLoad()
        Sql.tables += TextBlockExposedTable
    }

    override fun create(bo: StereotypeBo): StereotypeBo {
        val created = super.create(bo)

        bo.localizations.forEach { localizedBo ->
            localizationTable.insert {
                it[stereotype] = bo.id.toLong()
                it[locale] = localizedBo.locale.toLong()
                it[localizedName] = localizedBo.localizedName
            }
        }

        return created
    }

    override fun read(entityId: EntityId<StereotypeBo>): StereotypeBo {
        val bo = super.read(entityId)

        bo.localizations = localizationTable
            .select { localizationTable.stereotype eq bo.id.toLong() }
            .map {
                StereotypeLocalizationBo(
                    it[localizationTable.locale].entityId(),
                    it[localizationTable.localizedName]
                )
            }

        return bo
    }

    override fun update(bo: StereotypeBo): StereotypeBo {
        super.update(bo)

        localizationTable.deleteWhere { localizationTable.stereotype eq bo.id.toLong() }

        bo.localizations.forEach { localizedBo ->
            localizationTable.insert {
                it[stereotype] = bo.id.toLong()
                it[locale] = localizedBo.locale.toLong()
                it[localizedName] = localizedBo.localizedName
            }
        }

        return bo
    }

    override fun delete(entityId: EntityId<StereotypeBo>) {
        super.delete(entityId)
        localizationTable.deleteWhere { localizationTable.stereotype eq entityId.toLong() }
    }

    fun getId(locale : EntityId<LocaleBo>, localizedName : String) : EntityId<StereotypeBo> =
        localizationTable
            .select { localizationTable.localizedName eq localizedName }
            .andWhere { localizationTable.locale eq locale.toLong() }
            .first()
            .let { it[localizationTable.stereotype].entityId() }

}