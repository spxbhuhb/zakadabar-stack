/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.session.data

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import zakadabar.stack.Stack
import zakadabar.stack.backend.builtin.entities.data.EntityTable

object SessionTable : LongIdTable("t_${Stack.shid}_sessions") {
    val owner = reference("principal", EntityTable)
    val openedAt = datetime("opened_at")
    val lastActivity = datetime("last_activity")
}