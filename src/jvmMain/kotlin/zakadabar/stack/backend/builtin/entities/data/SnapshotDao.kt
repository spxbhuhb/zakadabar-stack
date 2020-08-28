/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SnapshotDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SnapshotDao>(SnapshotTable)

    var entity by EntityDao referencedOn SnapshotTable.entity
    var revision by SnapshotTable.revision
    var size by SnapshotTable.size
    var content by SnapshotTable.content
}