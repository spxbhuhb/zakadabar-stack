/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.record

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
import zakadabar.stack.backend.data.query.QueryBackend
import zakadabar.stack.backend.data.recordId
import zakadabar.stack.backend.server
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.data.record.*
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for record backends. Supports CRUD, queries and BLOBs.
 */
abstract class RecordBackend<T : RecordDto<T>>(
    val blobTable: BlobTable? = null,
    val recordTable: IdTable<Long>? = null
) : BackendModule, ActionBackend, QueryBackend {

    /**
     * The class of DTO this record backend servers. Namespace is automatically
     * set to the namespace defined for this DTO class.
     */
    abstract val dtoClass: KClass<T>

    /**
     * The namespace this backend serves. Must be unique in a server. Default
     * is the namespace of the DTO class.
     */
    override val namespace
        get() = (dtoClass.companionObject !!.objectInstance as RecordDtoCompanion<*>).dtoNamespace

    /**
     * Logger to use when logging is enabled. Name is [namespace].
     */
    override val logger by lazy { LoggerFactory.getLogger(namespace) !! }

    /**
     * When true (this is the default), actions are logged by the backend.
     */
    override var logActions = true

    /**
     * When true, POST and PATCH validates incoming DTO objects. When invalid
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
     * Create a new record.
     *
     * URL: `POST /api/<namespace>/record`
     *
     * @param executor Executor of the operation.
     * @param dto The record to create.
     *
     * @return DTO of the record created
     */
    open fun create(executor: Executor, dto: T): T {
        throw NotImplementedError()
    }

    /**
     * Read a record.
     *
     * URL: `GET /api/<namespace>/record/<recordId>`
     *
     * @param executor Executor of the operation.
     * @param recordId The id of the record to read.
     *
     * @return DTO of the record
     */
    open fun read(executor: Executor, recordId: RecordId<T>): T {
        throw NotImplementedError()
    }

    /**
     * Read a record with access to Ktor's application call. The
     * default implementation simply calls read without [call].
     *
     * URL: `GET /api/<namespace>/record/<recordId>`
     *
     * @param call     Ktor's [ApplicationCall].
     * @param executor Executor of the operation.
     * @param recordId The id of the record to read.
     *
     * @return DTO of the record
     */
    open fun read(call: ApplicationCall, executor: Executor, recordId: RecordId<T>) = read(executor, recordId)

    /**
     * Update an existing record.
     *
     * URL: `PATCH /api/<namespace>/record/<recordId>`
     *
     * Id comes from the [dto] parameter.
     *
     * @param executor Executor of the operation.
     * @param dto Update.
     *
     * @return DTO of the updated record.
     */
    open fun update(executor: Executor, dto: T): T {
        throw NotImplementedError()
    }

    /**
     * Delete a record.
     *
     * URL: `DELETE /api/<namespace>/record/<recordId>`
     *
     * @param executor Executor of the operation.
     * @param recordId Id of the record to delete.
     */
    open fun delete(executor: Executor, recordId: RecordId<T>) {
        throw NotImplementedError()
    }

    /**
     * List all records of this type.
     *
     * URL: `GET /api/<namespace>/record`
     *
     * @param executor Executor of the operation.
     *
     * @return list of DTOs of all records
     */
    open fun all(executor: Executor): List<T> {
        throw NotImplementedError()
    }

    /**
     * Called by blob functions to authorize the blob operation.
     */
    open fun blobAuthorize(executor: Executor, operation: BlobOperation, recordId: RecordId<T>?, blobId: RecordId<BlobDto>? = null, dto: BlobDto? = null) {
        throw Forbidden()
    }

    /**
     * Get metadata of blobs that belong to this record.
     *
     * URL : `GET /api/<namespace>/blob/list/<bid>`
     *
     * @param executor Executor of the operation.
     * @param recordId The record to get blob metadata for.
     *
     * @return DTO of the blobs
     */
    open fun blobMetaList(executor: Executor, recordId: RecordId<T>): List<BlobDto> {
        if (blobTable == null) throw NotImplementedError("missing blob table")

        blobAuthorize(executor, BlobOperation.MetaRead, recordId)

        return transaction {
            with(blobTable) {
                slice(id, dataRecord, name, type, size)
                    .select { dataRecord eq recordId.toLong() }
                    .map { toDto(it, namespace) }
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
     * @return DTO of the blobs
     */
    open fun blobMetaRead(executor: Executor, blobId: RecordId<T>): BlobDto {
        if (blobTable == null) throw NotImplementedError("missing blob table")

        val dto = transaction {
            with(blobTable) {
                toDto(
                    slice(id, dataRecord, name, type, size)
                        .select { id eq blobId.toLong() }
                        .first(),
                    namespace
                )
            }
        }

        @Suppress("UNCHECKED_CAST") // it is fine, we are working in this namespace
        blobAuthorize(executor, BlobOperation.MetaRead, dto.dataRecord as RecordId<T>, dto.id)

        return dto
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
    open fun blobRead(executor: Executor, blobId: RecordId<BlobDto>): Pair<BlobDto, ByteArray> {
        if (blobTable == null) throw NotImplementedError("missing blob table")

        val result = transaction {
            blobTable
                .select { blobTable.id eq blobId.toLong() }
                .map { blobTable.toDto(it, namespace) to it[blobTable.content].bytes }
                .firstOrNull() ?: throw NotFoundException()
        }

        @Suppress("UNCHECKED_CAST") // it is fine, we are working in this namespace
        blobAuthorize(executor, BlobOperation.Read, result.first.dataRecord as? RecordId<T>, blobId)

        return result
    }

    /**
     * Create a new blob.
     *
     * URL : `POST /api/<namespace>/blob`
     *
     * @param executor Executor of the operation.
     * @param dto The DTO of the blob to create.
     * @param bytes Content of the blob.
     *
     * @return Dto of the blob created.
     */
    open fun blobCreate(executor: Executor, dto: BlobDto, bytes: ByteArray): BlobDto {
        if (blobTable == null || recordTable == null) throw NotImplementedError("missing blob or record table")

        @Suppress("UNCHECKED_CAST") // can't do much with this
        blobAuthorize(executor, BlobOperation.Create, dto.dataRecord as? RecordId<T>, dto = dto)

        return transaction {
            val id = blobTable.insert {
                it[dataRecord] = if (dto.dataRecord == null) null else EntityID(dto.dataRecord !!.toLong(), recordTable)
                it[name] = dto.name
                it[type] = dto.type
                it[size] = dto.size
                it[content] = ExposedBlob(bytes)
            } get blobTable.id

            dto.copy(id = LongRecordId(id.value))
        }
    }

    /**
     * Update metadata of a blob.
     *
     * URL : `PATCH /api/<namespace>/blob/<blobId>
     *
     * @param executor Executor of the operation.
     * @param dto DTO of the blob
     *
     * @return Content of the blob (binary).
     */
    open fun blobMetaUpdate(executor: Executor, dto: BlobDto): BlobDto {

        if (blobTable == null || recordTable == null) throw NotImplementedError("missing blob or record table")

        @Suppress("UNCHECKED_CAST") // can't do much about this
        blobAuthorize(executor, BlobOperation.MetaUpdate, recordId = dto.dataRecord as? RecordId<T>?, blobId = dto.id, dto = dto)

        transaction {
            with(blobTable) {
                update({ id eq dto.id.toLong() }) {
                    it[dataRecord] = dto.dataRecord?.let { EntityID(it.toLong(), recordTable) }
                    it[name] = dto.name
                    it[type] = dto.type
                }
            }
        }

        return dto
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
    open fun blobDelete(executor: Executor, blobId: RecordId<BlobDto>) {
        if (blobTable == null) throw NotImplementedError("missing blob table")

        transaction {
            val dataRecordId = blobTable
                .slice(blobTable.dataRecord)
                .select { blobTable.id eq blobId.toLong() }
                .first()[blobTable.dataRecord]?.recordId<DtoBase>()

            @Suppress("UNCHECKED_CAST") // can't do much about this
            blobAuthorize(executor, BlobOperation.MetaUpdate, dataRecordId as RecordId<T>?, blobId)

            blobTable.deleteWhere { blobTable.id eq blobId.toLong() }
        }
    }

    /**
     * Adds CRUD routes for this record backend. Check crud functions for URLs.
     */
    fun Route.crud() {
        route("$namespace/record") {

            post {
                val executor = call.executor()
                val request = call.receive(dtoClass)

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
                    call.respond(read(call, executor, StringRecordId(id)) as Any)
                }
            }

            patch("/{rid}") {
                val executor = call.executor()
                val request = call.receive(dtoClass)

                if (validate && ! request.isValid) throw BadRequestException("invalid request")

                logger.info("${executor.accountId}: UPDATE $request")
                call.respond(update(executor, request) as Any)
            }

            delete("/{rid}") {
                val executor = call.executor()
                val id = call.parameters["rid"] ?: throw BadRequestException("missing record id")
                logger.info("${executor.accountId}: DELETE $id")
                call.respond(delete(executor, StringRecordId(id)) as Any)
            }
        }
    }

    /**
     * Adds BLOB routes for this backend. Check blob functions for URLs.
     */
    fun Route.blob() {
        route("$namespace/blob") {

            get("/list/{rid}") {
                val dataRecordId = call.parameters["rid"] ?: throw BadRequestException("missing data record id")

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-META-ALL $dataRecordId")

                apiCacheControl(call)

                call.respond(blobMetaList(executor, StringRecordId(dataRecordId)))
            }

            get("/meta/{bid}") {
                val blobId = call.parameters["bid"] ?: throw BadRequestException("missing blob id")

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-META $blobId")

                apiCacheControl(call)

                call.respond(blobMetaRead(executor, StringRecordId(blobId)))
            }

            get("/content/{bid}") {
                val blobId = call.parameters["bid"] ?: throw BadRequestException("missing blob id")

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-READ  $blobId")

                val (dto, bytes) = blobRead(executor, StringRecordId(blobId))

                // TODO add creation-date, modification-date
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment
                        .withParameter(ContentDisposition.Parameters.FileName, dto.name)
                        .withParameter(ContentDisposition.Parameters.Size, dto.size.toString())
                        .toString()
                )

                call.respondBytes(bytes)
            }

            patch("/meta/{bid}") {
                val executor = call.executor()
                val request = call.receive(BlobDto::class)

                if (validate && ! request.isValid) throw BadRequestException("invalid request")

                logger.info("${executor.accountId}: BLOB-UPDATE $namespace $request")
                call.respond(blobMetaUpdate(executor, request))
            }

            post("/{rid?}") {

                val recordId: RecordId<DtoBase>? = call.parameters["rid"]?.let { StringRecordId(it) }

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-CREATE $recordId")

                val headers = call.request.headers

                val length = headers["Content-Length"]?.toIntOrNull() ?: throw BadRequestException("missing content length")
                val disposition = headers["Content-Disposition"] ?: throw BadRequestException("missing content disposition")

                val dto = BlobDto(
                    id = EmptyRecordId(),
                    dataRecord = recordId,
                    namespace = namespace,
                    name = parseHeaderValue(disposition).single().params.find { it.name == "filename" }?.value ?: throw BadRequestException("missing filename"),
                    type = headers["Content-Type"] ?: "application/octet-stream",
                    size = length.toLong()
                )

                if (validate && ! dto.isValid) throw BadRequestException("invalid request")

                val bytes = ByteArray(length)

                call.receiveChannel().readFully(bytes, 0, length)

                call.respond(blobCreate(executor, dto, bytes))
            }

            delete("/{bid}") {
                val blobId = call.parameters["bid"] ?: throw BadRequestException("missing blob id")

                val executor = call.executor()

                if (Server.logReads) logger.info("${executor.accountId}: BLOB-DELETE $blobId")

                call.respond(blobDelete(executor, StringRecordId(blobId)))
            }

        }
    }

}