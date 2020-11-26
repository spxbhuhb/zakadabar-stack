/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.blob

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BlobDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<BlobDao>(BlobTable)

    var name by BlobTable.name
    var type by BlobTable.type
    var size by BlobTable.size
    var content by BlobTable.content

}