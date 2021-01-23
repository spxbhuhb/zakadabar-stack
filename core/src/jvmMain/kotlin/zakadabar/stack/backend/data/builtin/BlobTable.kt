/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.BlobDto

/**
 * Base table for storing binary data (images, files). [RecordBackend] support
 * for BLOB operations requires a table that extends this one.
 */
abstract class BlobTable(name: String, recordTable: LongIdTable) : LongIdTable(name) {

    val dataRecord = reference("data_record", recordTable).nullable()
    val name = varchar("name", 100)
    val type = varchar("type", 50)
    val size = long("size")
    val content = blob("content")

    fun toDto(row: ResultRow, recordType: String) = BlobDto(
        id = row[id].value,
        dataRecord = row[dataRecord]?.value,
        dataType = recordType,
        name = row[name],
        type = row[type],
        size = row[size]
    )

}