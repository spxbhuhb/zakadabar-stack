/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.ktor

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import zakadabar.core.backend.business.EntityBusinessLogicCommon
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.entity.EntityId

open class KtorEntityRouter<T : EntityBo<T>>(
    private val entityBusinessLogic: EntityBusinessLogicCommon<T>
) : KtorRouter<T>(entityBusinessLogic) {

    override var qualifier = "entity"

    override fun installRoutes(context: Any) {

        super.installRoutes(context)

        with(context as Route) {
            route("${entityBusinessLogic.namespace}/${qualifier}") {

                get {
                    list(call)
                }

                get("{rid}") {
                    call.parameters["rid"]?.let { read(call, it) }
                }

                post {
                    create(call)
                }

                patch("/{rid}") {
                    update(call)
                }

                delete("/{rid}") {
                    delete(call)
                }
            }
        }
    }

    suspend fun list(call: ApplicationCall) {

        val executor = call.executor()

        apiCacheControl(call)

        call.respond(entityBusinessLogic.listWrapper(executor) as Any)

    }

    open suspend fun read(call: ApplicationCall, id: String) {

        val executor = call.executor()

        apiCacheControl(call)

        call.respond(entityBusinessLogic.readWrapper(executor, EntityId(id)) as Any)

    }

    suspend fun create(call: ApplicationCall) {

        val executor = call.executor()
        val request = call.receive(entityBusinessLogic.boClass)

        call.respond(entityBusinessLogic.createWrapper(executor, request) as Any)

    }

    suspend fun update(call: ApplicationCall) {

        val executor = call.executor()
        val request = call.receive(entityBusinessLogic.boClass)

        call.respond(entityBusinessLogic.updateWrapper(executor, request) as Any)

    }

    suspend fun delete(call: ApplicationCall) {

        val id = call.parameters["rid"] ?: throw BadRequestException("missing id")
        val executor = call.executor()

        entityBusinessLogic.deleteWrapper(executor, EntityId(id))

        call.respond(HttpStatusCode.OK)

    }

}