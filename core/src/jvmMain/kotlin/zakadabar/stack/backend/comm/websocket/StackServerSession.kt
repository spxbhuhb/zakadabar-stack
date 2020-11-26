/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.comm.websocket

import io.ktor.http.cio.websocket.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.builtin.blob.BlobDao
import zakadabar.stack.backend.builtin.entities.data.*
import zakadabar.stack.backend.builtin.entities.data.EntityTable.name
import zakadabar.stack.backend.builtin.session.data.SessionDao
import zakadabar.stack.backend.builtin.session.data.SessionTable
import zakadabar.stack.backend.util.ContentBlob
import zakadabar.stack.backend.util.fine
import zakadabar.stack.backend.util.sql
import zakadabar.stack.comm.websocket.message.*
import zakadabar.stack.comm.websocket.serialization.ResponseCode
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray
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
    private lateinit var session: SessionDao

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
                        is ReadBlobRequest -> onFetchContent(request)
                        is WriteBlobRequest -> onPushContent(request)
                        is ListEntitiesRequest -> onListEntities(request)
                        is AddEntityRequest -> onAddEntity(request)
                        is CreateBlobRequest -> onCreateBlob(request)
                        is GetBlobMetaRequest -> onGetBlobMeta(request)
                        is CloseStackSessionRequest -> onCloseSession()
                        else -> FaultResponse(ResponseCode.UNKNOWN_MESSAGE_TYPE)
                    }
                } catch (ex: EntityNotFoundException) {
                    FaultResponse(ResponseCode.NOT_FOUND)
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

            session = SessionDao.new {
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

        val session = SessionDao.findById(session.id) ?: return@sql CloseStackSessionResponse(ResponseCode.OK)

        // TODO better error handling here, I think
        session.locks.forEach { lock ->
            ContentBlob(lock.content).delete()
            SnapshotTable.deleteWhere { (SnapshotTable.entity eq lock.entity.id) and (SnapshotTable.revision eq lock.revision) }
            Locks.deleteWhere { Locks.id eq lock.id }
        }

        SessionTable.deleteWhere { SessionTable.id eq session.id }

        logger.info("$logTag closed transfer session $sessionUuid")

        CloseStackSessionResponse(ResponseCode.OK)
    }

    private suspend fun onAddEntity(request: AddEntityRequest) = sql {

        val parentId = request.parentId

        val parent = if (parentId == null) null else EntityDao[parentId]

        val now = Clock.System.now().toJavaInstant()

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

    private suspend fun onFetchContent(request: ReadBlobRequest) = sql {

        if (request.size > MAX_FETCH_SIZE) return@sql FaultResponse(ResponseCode.REQUEST_SIZE_LIMIT)

        val dao = BlobDao[request.blobId]
        val blob = ContentBlob(dao.content)
        val data = blob.read(request.position, request.size)

        ReadBlobResponse(ResponseCode.OK, request.blobId, request.position, data)
    }

    private suspend fun onListEntities(request: ListEntitiesRequest) = sql {

        val entities = EntityDao.find { EntityTable.parent eq request.parent }.map { it.toDto() }

        ListEntitiesResponse(ResponseCode.OK, entities)
    }

    private suspend fun onCreateBlob(request: CreateBlobRequest) = sql {

        val blob = BlobDao.new {
            name = request.name
            type = request.type
            size = request.size
            content = ContentBlob().create().id
        }

        CreateBlobResponse(ResponseCode.OK, blob.id.value)

    }

    private suspend fun onGetBlobMeta(request: GetBlobMetaRequest) = sql {
        with(BlobDao[request.blobId]) {
            GetBlobMetaResponse(ResponseCode.OK, id.value, name, type, size)
        }
    }

    private suspend fun onPushContent(request: WriteBlobRequest) = sql {

        val dao = BlobDao[request.blobId]

        val blob = ContentBlob(dao.content)
        blob.write(request.position, request.data)

        val requestEnd = request.position + request.data.size
        if (dao.size < requestEnd) {
            dao.size = requestEnd
        }

        logger.info("$logTag blob write ${dao.id} at ${request.position}, ${request.data.size} bytes")

        WriteBlobResponse(ResponseCode.OK, request.blobId, request.position, request.data.size)
    }


}