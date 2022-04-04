/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.business

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.route.BusinessLogicRouter
import zakadabar.core.server.ktor.KtorEntityRouter
import zakadabar.core.server.ktor.executor
import zakadabar.core.util.PublicApi
import zakadabar.core.util.after
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.lib.blobs.persistence.BlobExposedPa
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for BLOB business logics.
 */
abstract class BlobBlBase<T : BlobBo<T, RT>, RT : EntityBo<RT>>(
    boClass: KClass<T>,
    override val pa: BlobExposedPa<T, RT>
) : EntityBusinessLogicBase<T>(
    boClass
) {

    override val namespace
        get() = (boClass.companionObject !!.objectInstance as BlobBoCompanion<*, *>).boNamespace

    override val router: BusinessLogicRouter<T> by after {
        object : KtorEntityRouter<T>(this) {

            override var qualifier = "blob/meta"

            override fun installRoutes(context: Any) {
                super.installRoutes(context)
                with(context as Route) {
                    get("${namespace}/blob/content/{blobId}") {
                        readContent(call)
                    }
                    post("${namespace}/blob/content/{blobId}") {
                        writeContent(call)
                    }
                    get("${namespace}/blob/list/{referenceId?}") {
                        byReference(call)
                    }
                }
            }
        }
    }

    open override fun create(executor: Executor, bo: T): T {
        bo.size = 0 // there is no data uploaded yet
        return super.create(executor, bo)
    }

    open suspend fun readContent(call: ApplicationCall) {
        val blobId = call.parameters["blobId"]?.let { EntityId<T>(it) } ?: throw BadRequestException("missing blob id")

        val (bytes, mimeType) = readContent(call.executor(), blobId) { bo ->
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment
                    .withParameter(ContentDisposition.Parameters.FileName, bo.name)
                    .withParameter(ContentDisposition.Parameters.Size, bo.size.toString())
                    .toString()
            )
        }

        call.respondBytes(bytes, ContentType.parse(mimeType))
    }

    open fun readContent(executor: Executor, blobId: EntityId<T>, callback : (T) -> Unit): Pair<ByteArray, String> =
        pa.withTransaction {
            val bo = pa.read(blobId)

            authorizer.authorizeRead(executor, bo.id)

            auditor.auditRead(executor, bo.id)

            callback(bo)

            pa.readContent(bo.id) to bo.mimeType
        }

    open suspend fun writeContent(call: ApplicationCall) {
        val blobId = call.parameters["blobId"]?.let { EntityId<T>(it) } ?: throw BadRequestException("missing blob id")

        val headers = call.request.headers
        val length = headers["Content-Length"]?.toIntOrNull() ?: throw BadRequestException("missing content length")

        val bytes = ByteArray(length)
        call.receiveChannel().readFully(bytes, 0, length)

        writeContent(call.executor(), blobId, length.toLong(), bytes)

        call.respond(HttpStatusCode.OK, "received")
    }

    open fun writeContent(executor: Executor, blobId: EntityId<T>, length : Long, bytes : ByteArray) = pa.withTransaction {
        val bo = pa.read(blobId)

        authorizer.authorizeCreate(executor, bo)

        bo.size = length
        pa.update(bo)

        pa.writeContent(blobId, bytes)

        auditor.auditUpdate(executor, bo)
    }

    open suspend fun byReference(call: ApplicationCall) {
        val referenceId = call.parameters["referenceId"]?.let { EntityId<RT>(it) }
        val disposition = call.parameters["disposition"]

        val result = byReference(call.executor(), referenceId, disposition)

        // Without Any the compiler throws an exception because non-reified
        // types with recursive bounds are not supported yet.

        call.respond(result as Any)
    }

    open fun byReference(executor: Executor, referenceId: EntityId<RT>?, disposition: String?): List<T> =
        pa.withTransaction {

            // FIXME this should use the authorizer of the reference

            authorizer.authorizeList(executor)

            auditor.auditList(executor)

            pa.byReference(referenceId, disposition)
        }

        /**
     * List blobs with the given reference and entity id.
     *
     * @param  entityId  The reference to list the blobs for,
     * @param  disposition   The disposition to filter for, when `null`, all blobs are included.
     *
     * @return list of blob BOs for the given entity with the given disposition
     */
    @PublicApi
    open fun byReference(entityId: EntityId<RT>, disposition: String? = null) =
        pa.byReference(entityId, disposition)

}