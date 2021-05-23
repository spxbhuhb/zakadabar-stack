/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.builtin

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.lib.examples.data.builtin.ExampleReferenceDto
import zakadabar.stack.backend.data.entityId

object ExampleReferenceTable : LongIdTable("example_reference") {

    val name = varchar("stringValue", 50)

    fun toDto(row: ResultRow) = ExampleReferenceDto(
        id = row[id].entityId(),
        name = row[name]
    )

}