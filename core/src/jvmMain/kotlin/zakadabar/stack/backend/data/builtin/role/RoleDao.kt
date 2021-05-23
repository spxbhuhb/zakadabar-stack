/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.role

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.data.builtin.account.RoleBo

class RoleDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RoleDao>(RoleTable)

    var name by RoleTable.name
    var description by RoleTable.description

    fun toBo() = RoleBo(
        id = id.entityId(),
        name = name,
        description = description
    )
}