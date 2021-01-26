/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.sea

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.demo.data.SeaDto

object SeaTable : LongIdTable("seas") {

    val name = varchar("name", 30)

    fun toDto(row: ResultRow) = SeaDto(
        id = row[id].value,
        name = row[name]
    )

}