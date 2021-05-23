/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.entity

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Forbidden
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.action.ActionBackend
import zakadabar.stack.backend.data.builtin.BlobTable
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.query.QueryBackend
import zakadabar.stack.backend.server
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.BlobBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for entity backends. Supports CRUD, queries and BLOBs.
 */
abstract class EntityBackend<T : EntityBo<T>>(
    val blobTable: BlobTable? = null,
    val entityTable: IdTable<Long>? = null
) : BackendModule, ActionBackend, QueryBackend {

    /**
     * The class of BO this entity backend servers. Namespace is automatically
     * set to the namespace defined for this BO class.
     */
    abstract val boClass: KClass<T>

    /**
     * The namespace this backend serves. Must be unique in a server. Default
     * is the namespace of the BO class.
     */
    override val namespace
        get() = (boClass.companionObject !!.objectInstance as EntityBoCompanion<*>).boNamespace

    /**
     * Logger to use when logging is enabled. Name is [namespace].
     */
    override val logger by lazy { LoggerFactory.getLogger(namespace) !! }

    /**
     * When true (this is the default), actions are logged by the backend.
     */
    override var logActions = true

    /**
     * When true, POST and PATCH validates incoming BO objects. When invalid
     * throws [BadRequest]. Default is from the [Server.validate] property.
     */
    var validate = Server.validate

    /**
     * Adds cache control directive to GET requests, except BLOB content.
     * Default implementation calls the function with the same name from the
     * server. The function in the server sets the Cache-Control header to the
     * value specified by the server settings. Default is "no-cache, no-store".
     */
    open fun apiCacheControl(call : ApplicationCall) {
        server.apiCacheControl(call)
    }

    /**
     * Create a new entity.
     *
     * URL: `POST /api/<namespace>/entity`
     *
     * @param executor Executor of the operation.
     * @param bo The entity to create.
     *
     * @return BO of the entity created
     */
    open fun create(executor: Executor, bo: T): T {
        throw NotImplementedError()
    }

    /**
     * Read an entity.
     *
     * URL: `GET /api/<namespace>/entity/<entityId>`
     *
     * @param executor Executor of the operation.
     * @param entityId The id of the entity to read.
     *
     * @return BO of the entity
     */
    open fun read(executor: Executor, entityId: EntityId<T>): T {
        throw NotImplementedError()
    }

    /**
     * Read an entity with access to Ktor's application call. The
     * default implementation simply calls read without [call].
     *
     * URL: `GET /api/<namespace>/entity/<entityId>`
     *
     * @param call     Ktor's [ApplicationCall].
     * @param executor Executor of the operation.
     * @param entityId The id of the entity to read.
     *
     * @return BO of the entity
     */
    open fun read(call: ApplicationCall, executor: Executor, entityId: EntityId<T>) = read(executor, entityId)

    /**
     * Update an existing entity.
     *
     * URL: `PATCH /api/<namespace>/entity/<entityId>`
     *
     * Id comes from the [bo] parameter.
     *
     * @param executor Executor of the operation.
     * @param bo Update.
     *
     * @return BO of the updated entity.
     */
    open fun update(executor: Executor, bo: T): T {
        throw NotImplementedError()
    }

    /**
     * Delete an entity.
     *
     * URL: `DELETE /api/<namespace>/entity/<entityId>`
     *
     * @param executor Executor of the operation.
     * @param entityId Id of the entity to delete.
     */
    open fun delete(executor: Executor, entityId: EntityId<T>) {
        throw NotImplementedError()
    }

    /**
     * List all entities of this namespace.
     *
     * URL: `GET /api/<namespace>/entity`
     *
     * @param executor Executor of the operation.
     *
     * @return list of BOs of all entities
     */
    open fun all(executor: Executor): List<T> {
        throw NotImplementedError()
    }

    /**
     * Called by blob functions to authorize the blob operation.
     */
    open fun blobAuthorize(executor: Executor, operation: BlobOperation, entityId: EntityId<T>?, blobId: EntityId<BlobBo>? = null, bo: BlobBo? = null) {
        throw Forbidden()
    }

    /**
     * Get metadata of blobs that belong to this entity.
     *
     * URL : `GET /api/<namespace>/blob/list/<bid>`
     *
     * @param executor Executor of the operation.
     * @param entityId The entity to get blob metadata for.
     *
     * @return BO of the blobs
     */
    open fun blobMetaList(executor: Executor, entityId: EntityId<T>): List<BlobBo> {
        if (blobTable == null) throw NotImplementedError("missing blob table")

        blobAuthorize(executor, BlobOperation.MetaRead, entityId)

        return transaction {
            with(blobTable) {
                slice(id, blobTable.entityId, name, type, size)
                    .select { blobTable.entityId eq entityId.toLong() }
                    .map { toBo(it, namespace) }
            }
        }
    }

    /**
     * Get metadata of one blob.
     *
     * URL : `GET /api/<namespace>/blob/meta/<bid>`
     *
     * @param executor Executor of the operation.
     * @param blobId Id of the blob to get metadata for.
     *
     * @return BO of the blobs
     */
    open fun blobMetaRead(executor: Executor, blobId: EntityId<T>): BlobBo {
        if (blobTable == null) throw NotImplementedError("missing blob table")

        val bo = transaction {
            with(blobTable) {
                toBo(
                    slice(id, entityId, name, type, size)
                        .select { id eq blobId.toLong() }
                        .first(),
                    namespace
                )
            }
        }

        @Suppress("UNCHECKED_CAST") // it is fine, we are working in this namespace
        blobAuthorize(executor, BlobOperation.MetaRead, bo.entityId as EntityId<T>, bo.id)

        return bo
    }

    /**
     * Get content of a blob. This is an optimistic approach that fetches the blob first and
     * checks authorization after.
     *
     * URL : `GET /api/<namespace>/blob/content/<blobId>`
     *
     * @param executor Executor of the operation.
     * @param blobId The id of the blob to get.
     *
     * @return Content of the blob (binary).
     */
    open fun blobRead(executor: Executor, blobId: EntityId<BlobBo>): Pair<BlobBo, ByteArray> {
        if (blobTable == null) throw NotImplementedError("missing blob table")

        val result = transaction {
            blobTable
                .select { blobTable.id eq blobId.toLong() }
                .map { blobTable.toBo(it, namespace) to it[blobTable.content].bytes }
                .firstOrNull() ?: throw NotFoundException()
        }

        @Suppress("UNCHECKED_CAST") // it is fine, we are working in this namespace
        blobAuthorize(executor, BlobOperation.Read, result.first.entityId as? EntityId<T>, blobId)

        return result
    }

    /**
     * Create a new blob.
     *
     * URL : `POST /api/<namespace>/blob`
     *
     * @param executor Executor of the operation.
     * @param bo The BO of the blob to create.
     * @param bytes Content of the blob.
     *
     * @return Bo of the blob created.
     */
    open fun blobCreate(executor: Executor, bo: BlobBo, bytes: ByteArray): BlobBo {
        if (blobTable == null || entityTable == null) throw NotImplementedError("missing blob or record table")

        @Suppress("UNCHECKED_CAST") // can't do much with this
        blobAuthorize(executor, BlobOperation.Create, bo.entityId as? EntityId<T>, bo = bo)

        return transaction {
            val id = blobTable.insert {
                it[entityId] = if (bo.entityId == null) null else EntityID(bo.entityId !!.toLong(), entityTable)
                it[name] = bo.name
                it[type] = bo.type
                it[size] = bo.size
                it[content] = ExposedBlob(bytes)
            } get blobTable.id

            bo.copy(id = EntityId(id.value))
        }
    }

    /**
     * Update metadata of a blob.
     *
     * URL : `PATCH /api/<namespace>/blob/<blobId>
     *
     * @param executor Executor of the operation.
     * @param bo BO of the blob
     *
     * @return Content of the blob (binary).
     */
    open fun blobMetaUpdate(executor: Executor, bo: BlobBo): BlobBo {

        if (blobTable == null || entityTable == null) throw NotImplementedError("missing blob or record table")

        @Suppress("UNCHECKED_CAST") // can't do much about this
        blobAuthorize(executor, BlobOperation.MetaUpdate, entityId = bo.entityId as? EntityId<T>?, blobId = bo.id, bo = bo)

        transaction {
            with(blobTable) {
                update({ id eq bo.id.toLong() }) {
                    it[entityId] = bo.entityId?.let { EntityID(it.toLong(), entityTable) }
                    it[name] = bo.name
                    it[type] = bo.type
                }
            }
        }

        return bo
    }

    /**
     * Delete a blob.
     *
     * URL : `DELETE /api/<namespace>/blob/<blobId>`
     *
     * @param executor Executor of the operation.
     * @param blobId Id of the blob to delete.
     *
     * @return Content of the blob (binary).
     */
    open fun blobDelete(executor: Executor, blobId: EntityId<BlobBo>) {
        if (blobTable == null) throw NotImplementedError("missing blob table")

        transaction {
            val dataEntityId = blobTable
                .slice(blobTable.entityId)
                .select { blobTable.id eq blobId.toLong() }
                .first()[blobTable.entityId]?.entityId<BaseBo>()

            @Suppress("UNCHECKED_CAST") // can't do much about this
            blobAuthorize(executor, BlobOperation.MetaUpdate, dataEntityId as EntityId<T>?, blobId)

            blobTable.deleteWhere { blobTable.id eq blobId.toLong() }
        }
    }

    /**
     * Adds CRUD routes for this entity backend. Check crud functions for URLs.
     */
    fun Route.crud() {
        route("$namespace/entity") {

            post {
                val executor = call.executor()
                val request = call.receive(boClass)

                if (validate && ! request.isValid) throw BadRequestException("invalid request")

                logger.info("${executor.accountId}: CREATE $request")
                call.respond(create(executor, request) as Any)
            }

            get("/{rid?}") {
                val id = call.parameters["rid"]
                val executor = call.executor()

                apiCacheControl(call)

                if (id == null) {
                    if (Server.logReads) logger.info("${executor.accountId}: ALL")
                    call.respond(all(executor) as Any)
                } else {
                    if (Server.logReads) logger.info("${executor.accountId}: READ $id")
                    call.respond(read(call, executor, EntityId(id)) as Any)
                }
            }

            patch("/{rid}") {
                val executor = call.executor()
                val request = call.receive(boClass)

                if (validate && ! request.isValid) throw BadRequestException("invalid request")

                logger.info("${executor.accountId}: UPDATE $request")
                call.respond(update(executor, request) as Any)
            }

            delete("/{rid}") {
                val executor = call.executor()
                val id = call.parameters["rid"] ?: throw BadRequestException("missing record id")
                logger.info("${executor.accountId}: DELETE $id")
                call.respond(delete(executor, EntityId(id)) as Any)
            }
        }
    }

    /**
     * Adds BLOB routes for this backend. Check blob functions for URLs.
     */
    fun Route.blob() {
        route("$namespace/blob") {

            get("/list/{rid}") {
                val parentEntityId = call.parameters["rid"] ?: throw BadRequestException("missing parent entity id")

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-META-ALL $parentEntityId")

                apiCacheControl(call)

                call.respond(blobMetaList(executor, EntityId(parentEntityId)))
            }

            get("/meta/{bid}") {
                val blobId = call.parameters["bid"] ?: throw BadRequestException("missing blob id")

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-META $blobId")

                apiCacheControl(call)

                call.respond(blobMetaRead(executor, EntityId(blobId)))
            }

            get("/content/{bid}") {
                val blobId = call.parameters["bid"] ?: throw BadRequestException("missing blob id")

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-READ  $blobId")

                val (bo, bytes) = blobRead(executor, EntityId(blobId))

                // TODO add creation-date, modification-date
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment
                        .withParameter(ContentDisposition.Parameters.FileName, bo.name)
                        .withParameter(ContentDisposition.Parameters.Size, bo.size.toString())
                        .toString()
                )

                call.respondBytes(bytes)
            }

            patch("/meta/{bid}") {
                val executor = call.executor()
                val request = call.receive(BlobBo::class)

                if (validate && ! request.isValid) throw BadRequestException("invalid request")

                logger.info("${executor.accountId}: BLOB-UPDATE $namespace $request")
                call.respond(blobMetaUpdate(executor, request))
            }

            post("/{rid?}") {

                val entityId: EntityId<BaseBo>? = call.parameters["rid"]?.let { EntityId(it) }

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-CREATE $entityId")

                val headers = call.request.headers

                val length = headers["Content-Length"]?.toIntOrNull() ?: throw BadRequestException("missing content length")
                val disposition = headers["Content-Disposition"] ?: throw BadRequestException("missing content disposition")

                val bo = BlobBo(
                    id = EntityId(),
                    entityId = entityId,
                    namespace = namespace,
                    name = parseHeaderValue(disposition).single().params.find { it.name == "filename" }?.value ?: throw BadRequestException("missing filename"),
                    type = headers["Content-Type"] ?: "application/octet-stream",
                    size = length.toLong()
                )

                if (validate && ! bo.isValid) throw BadRequestException("invalid request")

                val bytes = ByteArray(length)

                call.receiveChannel().readFully(bytes, 0, length)

                call.respond(blobCreate(executor, bo, bytes))
            }

            delete("/{bid}") {
                val blobId = call.parameters["bid"] ?: throw BadRequestException("missing blob id")

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-DELETE $blobId")

                call.respond(blobDelete(executor, EntityId(blobId)))
            }

        }
    }

}