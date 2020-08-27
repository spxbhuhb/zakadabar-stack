/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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