/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.blob

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.Stack
import zakadabar.stack.backend.util.OidBlobColumnType
import zakadabar.stack.data.BlobDto

object BigBlobTable : LongIdTable("t_${Stack.shid}_blobs") {
    val name = varchar("name", 100)
    val type = varchar("type", 50)
    val size = long("size")
    val content = registerColumn<Long>("content", OidBlobColumnType())

    fun toDto(row: ResultRow) = BlobDto(
        id = row[id].value,
        dataRecord = 0L,
        dataType = "",
        name = row[name],
        type = row[type],
        size = row[size]
    )

}