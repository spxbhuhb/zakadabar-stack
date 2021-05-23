/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.data.builtin.resources.LocaleBo

object LocaleTable : LongIdTable("locales") {

    val name = varchar("name", 100)
    val description = text("description")

    fun toBo(row: ResultRow) = LocaleBo(
        id = row[id].entityId(),
        name = row[name],
        description = row[description]
    )

}