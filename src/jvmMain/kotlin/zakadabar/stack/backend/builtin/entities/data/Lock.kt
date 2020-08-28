/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Lock(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Lock>(Locks)

    var entity by EntityDao referencedOn Locks.entity
    var session by Session referencedOn Locks.session
    var revision by Locks.revision
    var content by Locks.content

}