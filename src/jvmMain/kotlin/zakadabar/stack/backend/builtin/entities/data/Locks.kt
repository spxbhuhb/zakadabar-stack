/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities.data

import org.jetbrains.exposed.dao.id.LongIdTable
import zakadabar.stack.Stack
import zakadabar.stack.backend.builtin.session.data.SessionTable

object Locks : LongIdTable("t_${Stack.shid}_locks") {
    val entity = reference("entity", EntityTable).index()
    val session = reference("session", SessionTable)
    val revision = long("revision")
    val content = long("content")
}

