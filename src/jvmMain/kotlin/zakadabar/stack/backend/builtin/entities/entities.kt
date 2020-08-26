/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.stack.backend.builtin.entities

import io.ktor.features.*
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
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.util.Executor

fun queryEntities(executor: Executor, id: Long? = null, parentId: Long? = null): List<EntityDto> = transaction {

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


fun createEntity(executor: Executor, dto: EntityDto) = transaction {

    val entityDao = EntityDao.create(executor, dto)

    entityDao.toDto()

}

fun updateEntity(executor: Executor, dto: EntityDto) = transaction {

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

    entityDao.revision = snapshot.revision

}

fun resolve(executor: Executor, path: List<String>): List<EntityDto> = transaction {

    var parentId: Long? = null

    path.mapNotNull {
        val dao = EntityDao.find { (EntityTable.parent) eq parentId and (EntityTable.name eq it) }.firstOrNull()
            ?: return@mapNotNull null

        parentId = dao.id.value

        dao.requireReadFor(executor)
        dao.toDto()
    }

}