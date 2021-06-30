/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.backend

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.ktor.KtorRouter
import zakadabar.stack.backend.ktor.executor
import zakadabar.stack.backend.route.Router
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.after
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for BLOB business logics.
 */
abstract class BlobBlBase<T : BlobBo<T>>(
    boClass : KClass<T>,
    override val pa: BlobExposedPa<T>
) : EntityBusinessLogicBase<T>(
    boClass
) {

    override val namespace
        get() = (boClass.companionObject !!.objectInstance as BlobBoCompanion<*>).boNamespace

    override val router: Router<T> by after {
        object : KtorRouter<T>(this) {

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

    override fun create(executor: Executor, bo: T): T {
        bo.size = 0 // there is no data uploaded yet
        return super.create(executor, bo)
    }

    private suspend fun readContent(call: ApplicationCall) {
        val blobId = call.parameters["blobId"]?.let { EntityId<T>(it) } ?: throw BadRequestException("missing blob id")

        val executor = call.executor()

        val bytes = pa.withTransaction {
            val bo = pa.read(blobId)

            authorizer.authorizeRead(executor, bo.id)

            auditor.auditRead(executor, bo.id)

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment
                    .withParameter(ContentDisposition.Parameters.FileName, bo.name)
                    .withParameter(ContentDisposition.Parameters.Size, bo.size.toString())
                    .toString()
            )

            pa.readContent(bo.id)
        }

        call.respondBytes(bytes)
    }


    private suspend fun writeContent(call: ApplicationCall) {
        val blobId = call.parameters["blobId"]?.let { EntityId<T>(it) } ?: throw BadRequestException("missing blob id")

        val executor = call.executor()

        val headers = call.request.headers
        val length = headers["Content-Length"]?.toIntOrNull() ?: throw BadRequestException("missing content length")

        val bytes = ByteArray(length)
        call.receiveChannel().readFully(bytes, 0, length)

        pa.withTransaction {
            val bo = pa.read(blobId)

            authorizer.authorizeCreate(executor, bo)

            bo.size = length.toLong()
            pa.update(bo)

            pa.writeContent(blobId, bytes)

            auditor.auditUpdate(executor, bo)
        }

        call.respond(HttpStatusCode.OK, "received")
    }


    private suspend fun byReference(call: ApplicationCall) {
        val referenceId = call.parameters["referenceId"]?.let { EntityId<T>(it) }

        val executor = call.executor()

        val result = pa.withTransaction {

            // FIXME this should use the authorizer of the reference

            authorizer.authorizeList(executor)

            auditor.auditList(executor)

            pa.byReference(referenceId)
        }

        // Without Any the compiler throws an exception because non-reified
        // types with recursive bounds are not supported yet.

        call.respond(result as Any)
    }

}