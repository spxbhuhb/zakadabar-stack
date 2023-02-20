/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.persistence

import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.data.LocaleStatus

class LocaleExposedPa : LocaleExposedPaGen() {

    fun byName(name : String) =
        table
            .select { table.name eq name }
            .map { it.toBo() }
            .firstOrNull()


    fun byStatus(status: LocaleStatus?): List<LocaleBo> {
        val query = table.selectAll()

        status?.let {
            query.andWhere {
                table.status eq it
            }
        }

        return query.map { it.toBo() }
    }

}