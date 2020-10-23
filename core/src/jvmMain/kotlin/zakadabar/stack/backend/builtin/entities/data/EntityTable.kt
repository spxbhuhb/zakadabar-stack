/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities.data

import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import zakadabar.stack.Stack
import zakadabar.stack.backend.BackendContext
import zakadabar.stack.backend.builtin.entities.data.EntityTable.acl
import zakadabar.stack.backend.builtin.entities.data.EntityTable.createdAt
import zakadabar.stack.backend.builtin.entities.data.EntityTable.createdBy
import zakadabar.stack.backend.builtin.entities.data.EntityTable.modifiedAt
import zakadabar.stack.backend.builtin.entities.data.EntityTable.modifiedBy
import zakadabar.stack.backend.builtin.entities.data.EntityTable.name
import zakadabar.stack.backend.builtin.entities.data.EntityTable.parent
import zakadabar.stack.backend.builtin.entities.data.EntityTable.revision
import zakadabar.stack.backend.builtin.entities.data.EntityTable.size
import zakadabar.stack.backend.builtin.entities.data.EntityTable.status
import zakadabar.stack.backend.builtin.entities.data.EntityTable.type
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.util.Executor

/**
 * Stores entity records. Don't change this table directly as many mechanisms
 * are linked to entities.
 *
 * @property  acl         The access control list of the entity. Null if the ACL of the parent should be used.
 * @property  status      Status of the entity.
 * @property  parent      The parent entity. Null when there is no parent and this is a top-level entity.
 * @property  type        Type of the entity. Always use types prefixed with short id. Maximum 50 characters.
 * @property  name        Name of the entity. Maximum 100 characters.
 * @property  size        Size of the latest snapshot that belongs to the entity in bytes, 0 when there is no snapshot.
 * @property  revision    Revision of the entity, changed automatically whenever the entity is changed. It does
 *                        not change when children are added / removed / changed.
 * @property  createdAt   The time and date when the entity's been created.
 * @property  createdBy   The id of the entity that/who created this one.
 * @property  modifiedAt  The last time and date when the entity's been changed.
 * @property  modifiedBy  The id of the entity that/who most recently changed this entity.
 *
 */
object EntityTable : LongIdTable("t_${Stack.shid}_entities") {
    val acl = reference("acl", EntityTable).nullable()
    val status = enumeration("status", EntityStatus::class)
    val parent = reference("parent", EntityTable).nullable().index()
    val type = varchar("type", 50)
    val name = varchar("name", 100)
    val size = long("size")
    val revision = long("revision")
    val createdAt = timestamp("created_at")
    val createdBy = reference("created_by", EntityTable)
    val modifiedAt = timestamp("modified_at")
    val modifiedBy = reference("modified_by", EntityTable)

    fun toDto(row: ResultRow) = EntityRecordDto(
        id = row[id].value,
        status = row[status],
        acl = row[acl]?.value,
        parentId = row[parent]?.value,
        entityType = row[type],
        name = row[name],
        size = row[size],
        revision = row[revision],
        modifiedAt = row[modifiedAt].toKotlinInstant(),
        modifiedBy = row[modifiedBy].value
    )

    fun readableBy(executor: Executor, row: ResultRow) =
        BackendContext.hasReadAccess(executor, row[status], row[acl]?.value)

}