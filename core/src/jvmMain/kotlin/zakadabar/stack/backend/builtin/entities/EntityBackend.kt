/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.entities.data.*
import zakadabar.stack.backend.data.RecordBackend
import zakadabar.stack.backend.util.ContentBlob
import zakadabar.stack.backend.util.executor
import zakadabar.stack.backend.util.sql
import zakadabar.stack.data.entity.ChildrenQuery
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.util.Executor
import zakadabar.stack.util.PublicApi

@PublicApi
object EntityBackend : RecordBackend<EntityRecordDto>() {

    override val dtoClass = EntityRecordDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                EntityTable,
                SnapshotTable,
                Locks
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
        route.query(ChildrenQuery::class, ::search)

        with(route) {

            route(EntityRecordDto.recordType) {

                get("/{id}/revisions/{revision?}") {
                    val (name, data) = fetchContent(
                        call.executor(),
                        call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException("invalid entity id"),
                        call.parameters["revision"]?.toLongOrNull()
                    )

                    call.response.header(
                        HttpHeaders.ContentDisposition,
                        ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, name)
                            .toString()
                    )

                    call.respondBytes(data)
                }

                post("/{id}/revisions") {
                    pushContent(
                        call.executor(),
                        call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException("invalid entity id"),
                        call.receiveStream().readBytes() // FIXME add chunk support
                    )
                    call.respond(HttpStatusCode.Accepted)
                }

                get("resolve/{path...}") {
                    call.respond(resolve(call.executor(), path = call.parameters.getAll("path") !!))
                }

            }
        }
    }

    private fun search(executor: Executor, query: ChildrenQuery) = transaction {
        EntityTable
            .select { EntityTable.parent eq query.parentId }
            .filter { EntityTable.readableBy(executor, it) }
            .map(EntityTable::toDto)
    }

    override fun create(executor: Executor, dto: EntityRecordDto) = transaction {
        EntityDao.create(executor, dto).toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        EntityDao[recordId].requireReadFor(executor).toDto()
    }

    override fun update(executor: Executor, dto: EntityRecordDto) = transaction {
        EntityDao.update(executor, dto).toDto()
    }

    override fun delete(executor: Executor, recordId: Long) = transaction {
        EntityDao[recordId].requireControlFor(executor).delete()
    }

    private suspend fun fetchContent(executor: Executor, entityId: Long, revision: Long?) = sql {
        val entityDao = EntityDao[entityId].requireReadFor(executor)

        val select =
            Op.build { (SnapshotTable.entity eq entityId) and (SnapshotTable.revision eq (revision ?: entityDao.revision)) }

        val snapshot = SnapshotDao.find(select).firstOrNull() ?: throw NotFoundException("revision not found")

        val blob = ContentBlob(snapshot.content)

        entityDao.name to blob.read(0, snapshot.size.toInt())
    }

    // FIXME locking, check StackServerSession.onPushContent
    private suspend fun pushContent(executor: Executor, entityId: Long, data: ByteArray) = sql {

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

    private fun resolve(executor: Executor, path: List<String>): List<EntityRecordDto> = transaction {

        var parentId: Long? = null

        path.mapNotNull {
            val dao = EntityDao.find { (EntityTable.parent) eq parentId and (EntityTable.name eq it) }.firstOrNull()
                ?: return@mapNotNull null

            parentId = dao.id.value

            dao.requireReadFor(executor)
            dao.toDto()
        }

    }
}