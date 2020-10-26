/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.security.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.builtin.entities.data.EntityDao

class RoleGrantDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RoleGrantDao>(AclEntryTable)

    var role by EntityDao referencedOn AclEntryTable.subject

}