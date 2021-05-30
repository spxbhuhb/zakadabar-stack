/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.backend

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.data.builtin.BlobBo

/**
 * Base table for storing binary data (images, files).
 */
abstract class BlobTable(name: String, entityTable: LongIdTable) : LongIdTable(name) {

    val entityId = reference("entity_id", entityTable).nullable()
    val name = varchar("name", 100)
    val type = varchar("type", 50)
    val size = long("size")
    val content = blob("content")

    fun toBo(row: ResultRow, namespace: String) = BlobBo(
        id = row[id].entityId(),
        entityId = row[entityId]?.entityId(),
        namespace = namespace,
        name = row[name],
        type = row[type],
        size = row[size]
    )

}