/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.builtin.role.RoleDao
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.data.builtin.resources.SettingBo
import zakadabar.stack.data.builtin.resources.SettingSource

class SettingDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SettingDao>(SettingTable)

    var role by RoleDao optionalReferencedOn SettingTable.role
    var namespace by SettingTable.namespace
    var className by SettingTable.className
    var descriptor by SettingTable.descriptor

    fun toBo() = SettingBo(
        id = id.entityId(),
        role = role?.id?.entityId(),
        source = SettingSource.Database,
        namespace = namespace,
        className = className,
        descriptor = descriptor
    )
}