/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.port

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.demo.backend.sea.SeaTable
import zakadabar.demo.data.PortDto

object PortTable : LongIdTable("ports") {

    val name = varchar("name", 100)
    val sea = reference("sea", SeaTable)

    fun toDto(row: ResultRow) = PortDto(
        id = row[id].value,
        name = row[name],
        sea = row[sea].value
    )

}