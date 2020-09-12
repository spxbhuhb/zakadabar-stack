/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities

import io.ktor.features.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.builtin.entities.data.SnapshotDao
import zakadabar.stack.backend.builtin.entities.data.SnapshotTable
import zakadabar.stack.backend.util.ContentBlob
import zakadabar.stack.backend.util.sql
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.util.Executor

fun queryEntities(executor: Executor, id: Long? = null, parentId: Long? = null): List<EntityRecordDto> = transaction {

    val condition = if (id == null) {
        EntityTable.parent eq parentId
    } else {
        EntityTable.id eq id
    }

    EntityTable
        .select(condition)
        .filter { EntityTable.readableBy(executor, it) }
        .map(EntityTable::toDto)
}


fun createEntity(executor: Executor, dto: EntityRecordDto) = transaction {

    val entityDao = EntityDao.create(executor, dto)

    entityDao.toDto()

}

fun updateEntity(executor: Executor, dto: EntityRecordDto) = transaction {

    val entityDao = EntityDao.update(executor, dto)

    entityDao.toDto()

}

suspend fun fetchContent(executor: Executor, entityId: Long, revision: Long?) = sql {
    val entityDao = EntityDao[entityId].requireReadFor(executor)

    val select =
        Op.build { (SnapshotTable.entity eq entityId) and (SnapshotTable.revision eq (revision ?: entityDao.revision)) }

    val snapshot = SnapshotDao.find(select).firstOrNull() ?: throw NotFoundException("revision not found")

    val blob = ContentBlob(snapshot.content)

    entityDao.name to blob.read(0, snapshot.size.toInt())
}

// FIXME locking, check StackServerSession.onPushContent
suspend fun pushContent(executor: Executor, entityId: Long, data: ByteArray) = sql {

    val entityDao = EntityDao[entityId].requireWriteFor(executor)

    val snapshot = SnapshotDao.new {
        entity = entityDao
        revision = entityDao.revision + 1
        size = data.size.toLong()
        content = ContentBlob().create().id
    }

    val blob = ContentBlob(snapshot.content)
    blob.write(0, data)

    entityDao.modifiedAt = Clock.System.now().toJavaInstant()
    entityDao.modifiedBy = EntityDao[executor.entityId]
    entityDao.revision = snapshot.revision
    entityDao.size = data.size.toLong()

}

fun resolve(executor: Executor, path: List<String>): List<EntityRecordDto> = transaction {

    var parentId: Long? = null

    path.mapNotNull {
        val dao = EntityDao.find { (EntityTable.parent) eq parentId and (EntityTable.name eq it) }.firstOrNull()
            ?: return@mapNotNull null

        parentId = dao.id.value

        dao.requireReadFor(executor)
        dao.toDto()
    }

}