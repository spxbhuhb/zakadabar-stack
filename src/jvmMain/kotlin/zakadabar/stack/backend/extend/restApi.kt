/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.extend

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import zakadabar.stack.backend.util.executor
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass

@PublicApi
fun <T : Any> restApi(
    route: Route,
    backend: RestBackend<T>,
    dtoClass: KClass<T>,
    dtoType: String
) {
    with(route) {

        route(dtoType) {

            get {
                call.respond(backend.query(call.executor(), null, parentId = call.parameters["parent"]?.toLong(), call.parameters))
            }

            get("/{id}") {
                call.respond(backend.fetch(call.executor(), id = call.parameters["id"] !!.toLong(), call.parameters))
            }

            post {
                call.respond(backend.create(call.executor(), call.receive(dtoClass)))
            }

            patch {
                call.respond(backend.update(call.executor(), call.receive(dtoClass)))
            }

        }

    }
}