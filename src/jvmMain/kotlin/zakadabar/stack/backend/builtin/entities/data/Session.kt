/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Session(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Session>(Sessions)

    var owner by EntityDao referencedOn Sessions.owner
    var openedAt by Sessions.openedAt
    var lastActivity by Sessions.lastActivity

    val locks by Lock referrersOn Locks.session
}