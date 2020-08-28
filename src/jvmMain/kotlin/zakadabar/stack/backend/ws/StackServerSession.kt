/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ws

import io.ktor.http.cio.websocket.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.builtin.entities.data.*
import zakadabar.stack.backend.builtin.entities.data.EntityTable.name
import zakadabar.stack.backend.util.ContentBlob
import zakadabar.stack.backend.util.fine
import zakadabar.stack.backend.util.sql
import zakadabar.stack.comm.message.*
import zakadabar.stack.comm.serialization.ResponseCode
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.util.UUID
import java.time.LocalDateTime

/**
 * TODO cache snapshots and locks so we don't need to go to SQL for each request
 *
 * @property owner       Owner of the session.
 */
class StackServerSession(private val webSocketSession: WebSocketSession) {
    companion object {
        private val logger = LoggerFactory.getLogger("Transfer")

        const val MAX_FETCH_SIZE = 1000000
    }

    private val sessionUuid = UUID()

    private val logTag = sessionUuid.toString().substring(0, 6)

    private lateinit var owner: EntityDao
    private lateinit var session: Session

    private var open = false

    suspend fun runReceive() {

        try {
            openSession()
            if (! open) return

            loop@ for (frame in webSocketSession.incoming) {

                val request = StackInputCommArray(frame.readBytes()).readMessage()

                logger.fine { "$logTag RECEIVED: $request" }

                val response = try {
                    when (request) {
                        is FetchContentRequest -> onFetchContent(request)
                        is PushContentRequest -> onPushContent(request)
                        is ListEntitiesRequest -> onListEntities(request)
                        is AddEntityRequest -> onAddEntity(request)
                        is OpenSnapshotRequest -> onOpenSnapshot(request)
                        is CloseSnapshotRequest -> onCloseSnapshot(request)
                        is CloseStackSessionRequest -> onCloseSession()
                        else -> FaultResponse(ResponseCode.UNKNOWN_MESSAGE_TYPE)
                    }
                } catch (ex: Exception) {
                    logger.error("$sessionUuid receive error", ex)
                    FaultResponse(ResponseCode.INTERNAL_SERVER_ERROR)
                }

                webSocketSession.send(Frame.Binary(true, StackOutputCommArray().write(response).pack()))

                logger.fine { "$logTag SENT: $response" }

                if (response is CloseStackSessionResponse) break@loop
                if (response.responseCode == ResponseCode.INTERNAL_SERVER_ERROR) break@loop
            }

        } catch (ex: Exception) {
            logger.error("$sessionUuid fatal receive error", ex)
        }

        // fail-safe: clean up if the session is still open
        // TODO verify how websocket close / timeout works

        try {
            if (open) onCloseSession()
        } catch (ex: Exception) {
            logger.error("$sessionUuid fatal cleanup error", ex)
        }
    }

    private suspend fun openSession() {

        val request = StackInputCommArray(webSocketSession.incoming.receive().readBytes()).readMessage()

        logger.fine { "$logTag RECEIVED: $request" }

        val response = sql {
            if (request !is OpenStackSessionRequest) return@sql FaultResponse(ResponseCode.OPEN_EXPECTED)

            // FIXME add authentication
            owner = EntityDao.find { name eq "anonymous" }.firstOrNull() ?: return@sql OpenStackSessionResponse(
                ResponseCode.AUTHENTICATION_FAILED,
                sessionUuid
            )

            open = true

            val now = LocalDateTime.now()

            session = Session.new {
                owner = this@StackServerSession.owner
                openedAt = now
                lastActivity = now
            }

            logger.info("$logTag opened transfer session $sessionUuid ${session.id}")

            OpenStackSessionResponse(ResponseCode.OK, sessionUuid)
        }

        webSocketSession.send(Frame.Binary(true, StackOutputCommArray().write(response).pack()))

        logger.fine { "$logTag SENT: $response" }
    }

    private suspend fun onCloseSession() = sql {
        open = false

        val session = Session.findById(session.id) ?: return@sql CloseStackSessionResponse(ResponseCode.OK)

        // TODO better error handling here, I think
        session.locks.forEach { lock ->
            ContentBlob(lock.content).delete()
            SnapshotTable.deleteWhere { (SnapshotTable.entity eq lock.entity.id) and (SnapshotTable.revision eq lock.revision) }
            Locks.deleteWhere { Locks.id eq lock.id }
        }

        Sessions.deleteWhere { Sessions.id eq session.id }

        logger.info("$logTag closed transfer session $sessionUuid")

        CloseStackSessionResponse(ResponseCode.OK)
    }

    private suspend fun onAddEntity(request: AddEntityRequest) = sql {

        val parentId = request.parentId

        val parent = if (parentId == null) null else EntityDao[parentId]

        val now = LocalDateTime.now()

        val entity = EntityDao.new {
            name = request.name
            acl = parent?.acl ?: EntityDao[1L] // FIXME set a proper domain
            this.parent = parent
            status = EntityStatus.Active
            type = request.type
            size = 0
            revision = 0
            createdAt = now
            createdBy = owner
            modifiedAt = now
            modifiedBy = owner
        }

        logger.info("$logTag added entity $request")

        AddEntityResponse(ResponseCode.OK, entity.id.value)
    }

    private suspend fun onFetchContent(request: FetchContentRequest) = sql {

        if (request.size > MAX_FETCH_SIZE) return@sql FaultResponse(ResponseCode.REQUEST_SIZE_LIMIT)

        val snapshot = SnapshotDao.findById(request.snapshotId) ?: return@sql FaultResponse(ResponseCode.NOT_FOUND)

        val blob = ContentBlob(snapshot.content)
        val data = blob.read(request.position, request.size)

        FetchContentResponse(ResponseCode.OK, request.snapshotId, request.position, data)
    }

    private suspend fun onListEntities(request: ListEntitiesRequest) = sql {

        val entities = EntityDao.find { EntityTable.parent eq request.parent }.map { it.toDto() }

        ListEntitiesResponse(ResponseCode.OK, entities)
    }

    private suspend fun onOpenSnapshot(request: OpenSnapshotRequest) = sql {
        if (request.revision == null) {
            val e = EntityDao.findById(request.entityId) ?: return@sql FaultResponse(ResponseCode.NOT_FOUND)

            val snapshot = SnapshotDao.new {
                entity = e
                revision = e.revision + 1
                size = 0
                content = ContentBlob().create().id
            }

            Lock.new {
                entity = e
                this.session = this@StackServerSession.session
                revision = snapshot.revision
                content = snapshot.content
            }

            OpenSnapshotResponse(ResponseCode.OK, snapshot.id.value, snapshot.revision)

        } else {

            val entity = EntityDao.findById(request.entityId) ?: return@sql FaultResponse(ResponseCode.NOT_FOUND)

            // FIXME check this out, too late, had a few beers, no idea why the compiler started to complain about this all of sudden
            val requestRevision = request.revision !!

            if (entity.revision < requestRevision) return@sql FaultResponse(ResponseCode.NOT_AVAILABLE_YET)

            val select =
                Op.build { (SnapshotTable.entity eq request.entityId) and (SnapshotTable.revision eq requestRevision) }

            val snapshot = SnapshotDao.find(select).firstOrNull() ?: return@sql FaultResponse(ResponseCode.NOT_FOUND)

            OpenSnapshotResponse(ResponseCode.OK, snapshot.id.value, snapshot.revision, snapshot.size)
        }
    }

    private suspend fun onCloseSnapshot(request: CloseSnapshotRequest) = sql {

        val snapshot = SnapshotDao.findById(request.snapshotId) ?: return@sql FaultResponse(ResponseCode.NOT_FOUND)

        if (snapshot.entity.revision < snapshot.revision) {
            val lock = getLock(snapshot) ?: return@sql FaultResponse(ResponseCode.MISSING_LOCK)

            snapshot.entity.size = snapshot.size
            snapshot.entity.revision = snapshot.revision

            Locks.deleteWhere { Locks.id eq lock.id }
        }

        CloseSnapshotResponse(ResponseCode.OK, request.snapshotId, snapshot.revision)
    }

    private suspend fun onPushContent(request: PushContentRequest) = sql {

        val snapshot = SnapshotDao.findById(request.snapshotId) ?: return@sql FaultResponse(ResponseCode.NOT_FOUND)

        getLock(snapshot) ?: return@sql FaultResponse(ResponseCode.MISSING_LOCK)

        val blob = ContentBlob(snapshot.content)
        blob.write(request.position, request.data)

        val requestEnd = request.position + request.data.size
        if (snapshot.size < requestEnd) {
            snapshot.size = requestEnd
        }

        logger.info("$logTag content write on entity ${snapshot.entity.id} revision ${snapshot.revision} at ${request.position}, ${request.data.size} bytes")

        PushContentResponse(ResponseCode.OK, request.snapshotId, request.position, request.data.size)
    }

    private fun getLock(snapshot: SnapshotDao): Lock? {
        return Lock.find { (Locks.entity eq snapshot.entity.id) and (Locks.session eq session.id.value) }.firstOrNull()
    }
}