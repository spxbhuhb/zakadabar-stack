/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.security.data

import org.jetbrains.exposed.dao.id.IdTable
import zakadabar.stack.Stack
import zakadabar.stack.backend.builtin.entities.data.EntityTable

object AclEntryTable : IdTable<Long>("t_${Stack.shid}_acl_entries") {
    override val id = reference("id", EntityTable)

    val subject = reference("subject", EntityTable)
    val access = varchar("access", 40)
    val allow = bool("allow")

    override val primaryKey = PrimaryKey(id)
}