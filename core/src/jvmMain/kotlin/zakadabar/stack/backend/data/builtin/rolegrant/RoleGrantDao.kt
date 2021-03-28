/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.rolegrant

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.builtin.role.RoleDao
import zakadabar.stack.data.builtin.account.RoleGrantDto

class RoleGrantDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RoleGrantDao>(RoleGrantTable)

    var principal by PrincipalDao referencedOn RoleGrantTable.principal
    var role by RoleDao referencedOn RoleGrantTable.role

    fun toDto() = RoleGrantDto(
        id = id.value,
        principal = principal.id.value,
        role = role.id.value
    )
}