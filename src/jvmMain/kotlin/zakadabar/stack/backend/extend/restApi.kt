/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.extend

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import zakadabar.stack.backend.util.executor
import kotlin.reflect.KClass

fun <T : Any> entityRestApi(
    route: Route,
    backend: EntityRestBackend<T>,
    dtoClass: KClass<T>,
    dtoType: String
) {
    with(route) {

        route(dtoType) {

            get {
                call.respond(backend.query(call.executor(), parentId = call.parameters["parent"]?.toLong()))
            }

            get("/{id}") {
                val list = backend.query(call.executor(), id = call.parameters["id"] !!.toLong())
                if (list.isEmpty()) throw NotFoundException()
                call.respond(list[0])
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

fun <T : Any> recordRestApi(
    route: Route,
    backend: RecordRestBackend<T>,
    dtoClass: KClass<T>,
    dtoType: String
) {
    with(route) {

        route(dtoType) {

            get {
                call.respond(backend.query(call.executor()))
            }

            get("/{id}") {
                val list = backend.query(call.executor(), id = call.parameters["id"] !!.toLong())
                if (list.isEmpty()) throw NotFoundException()
                call.respond(list[0])
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