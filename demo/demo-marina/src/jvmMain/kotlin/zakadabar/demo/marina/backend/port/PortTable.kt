/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.backend.port

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.demo.marina.backend.sea.SeaTable
import zakadabar.demo.marina.data.PortDto
import zakadabar.stack.backend.data.recordId

object PortTable : LongIdTable("ports") {

    val name = varchar("name", 100)
    val sea = reference("sea", SeaTable)

    fun toDto(row: ResultRow) = PortDto(
        id = row[id].recordId(),
        name = row[name],
        sea = row[sea].recordId()
    )

}