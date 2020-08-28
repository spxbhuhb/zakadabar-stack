/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import zakadabar.stack.Stack
import zakadabar.stack.backend.util.executor

object Api {

    fun install(route: Route) {

        with(route) {

            route("${Stack.shid}/entities") {

                get {
                    call.respond(queryEntities(call.executor(), parentId = call.parameters["parent"]?.toLong()))
                }

                post {
                    call.respond(createEntity(call.executor(), call.receive()))
                }

                route("{id}") {

                    get {
                        val entity = queryEntities(call.executor(), id = call.parameters["id"] !!.toLong())
                        if (entity.isEmpty()) throw NotFoundException()
                        call.respond(entity[0])
                    }

                    patch {
                        call.respond(updateEntity(call.executor(), call.receive()))
                    }

                    get("revisions/{revision}") {
                        val (name, data) = fetchContent(
                            call.executor(),
                            call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException("invalid entity id"),
                            call.parameters["revision"]?.toLongOrNull()
                        )

                        call.response.header(
                            HttpHeaders.ContentDisposition,
                            ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, name)
                                .toString()
                        )

                        call.respondBytes(data)
                    }

                    post("revisions") {
                        pushContent(
                            call.executor(),
                            call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException("invalid entity id"),
                            call.receiveStream().readBytes() // FIXME add chunk support
                        )
                        call.respond(HttpStatusCode.Accepted)
                    }
                }

            }

            get("${Stack.shid}/resolve/{path...}") {
                call.respond(resolve(call.executor(), path = call.parameters.getAll("path") !!))
            }

        }
    }

}