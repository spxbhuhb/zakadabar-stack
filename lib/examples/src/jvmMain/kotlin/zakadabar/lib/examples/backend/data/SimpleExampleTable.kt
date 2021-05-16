/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.lib.examples.data.SimpleExampleDto
import zakadabar.stack.backend.data.recordId

object SimpleExampleTable : LongIdTable("simple_example") {

    val name = varchar("name", 30)

    fun toDto(row: ResultRow) = SimpleExampleDto(
        id = row[id].recordId(),
        name = row[name]
    )

}