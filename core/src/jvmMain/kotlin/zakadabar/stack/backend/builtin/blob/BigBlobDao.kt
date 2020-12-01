/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.blob

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BigBlobDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<BigBlobDao>(BigBlobTable)

    var name by BigBlobTable.name
    var type by BigBlobTable.type
    var size by BigBlobTable.size
    var content by BigBlobTable.content

}