/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.rolegrant

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.backend.account.account.AccountDao
import zakadabar.demo.backend.account.role.RoleDao
import zakadabar.demo.data.account.RoleGrantDto

class RoleGrantDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RoleGrantDao>(RoleGrantTable)

    var account by AccountDao referencedOn RoleGrantTable.account
    var role by RoleDao referencedOn RoleGrantTable.role

    fun toDto() = RoleGrantDto(
        id = id.value,
        account = account.id.value,
        role = role.id.value
    )
}