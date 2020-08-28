/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.security.data

import org.jetbrains.exposed.dao.id.IdTable
import zakadabar.stack.Stack
import zakadabar.stack.backend.builtin.entities.data.EntityTable

object RoleGrantTable : IdTable<Long>("t_${Stack.shid}_role_grants") {
    override val id = reference("id", EntityTable)

    val role = reference("subject", EntityTable)

    override val primaryKey = PrimaryKey(id)
}