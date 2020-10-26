/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.session.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.Lock
import zakadabar.stack.backend.builtin.entities.data.Locks

class SessionDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SessionDao>(SessionTable)

    var owner by EntityDao referencedOn SessionTable.owner
    var openedAt by SessionTable.openedAt
    var lastActivity by SessionTable.lastActivity

    val locks by Lock referrersOn Locks.session
}