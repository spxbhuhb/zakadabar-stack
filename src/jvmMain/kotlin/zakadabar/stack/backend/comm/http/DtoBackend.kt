/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.comm.http

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.createType

abstract class DtoBackend<T : RecordDto<T>> {

    abstract val dtoClass: KClass<T>

    open val dtoType
        get() = (dtoClass.companionObject !!.objectInstance as RecordDtoCompanion<*>).type

    open fun create(executor: Executor, dto: T): T {
        throw NotImplementedError()
    }

    open fun read(executor: Executor, id: Long): T {
        throw NotImplementedError()
    }

    open fun update(executor: Executor, dto: T): T {
        throw NotImplementedError()
    }

    open fun delete(executor: Executor, id: Long) {
        throw NotImplementedError()
    }

    open fun all(executor: Executor): List<T> {
        throw NotImplementedError()
    }

    /**
     * Install route handlers.
     *
     * @param  route  Ktor Route context for installing routes
     */
    open fun install(route: Route) = Unit

    /**
     * A function that is called when the module is loaded.
     *
     * When called the other modules may or may not be loaded.
     */
    open fun onLoad() = Unit

    /**
     * An initialization function that is called during system startup to
     * initialize this extension.
     *
     * When called all modules are loaded and the DB is accessible.
     */
    open fun init() = Unit

    /**
     * A cleanup function that is called during system shutdown to clean up
     * this extension. DB is still accessible at this point.
     */
    open fun cleanup() = Unit

    fun Route.crud() {
        route(dtoType) {

            post {
                call.respond(create(call.executor(), call.receive(dtoClass)))
            }

            get("/{id}") {
                val id = call.parameters["id"] ?: "all"
                if (id == "all") {
                    call.respond(all(call.executor()))
                } else {
                    call.respond(read(call.executor(), id = id.toLong()))
                }
            }

            patch("/{id}") {
                call.respond(update(call.executor(), call.receive(dtoClass)))
            }

            delete("/{id}") {
                call.respond(update(call.executor(), call.receive(dtoClass)))
            }
        }
    }

    fun <RQ : Any, RS : Any> Route.query(queryDto: KClass<RQ>, func: (Executor, RQ) -> RS) {
        get("$dtoType/${queryDto.simpleName}") {
            val qText = call.parameters["q"]
            requireNotNull(qText)
            val qObj = Json.decodeFromString(serializer(queryDto.createType()), qText)
            @Suppress("UNCHECKED_CAST")
            call.respond(func(call.executor(), qObj as RQ))
        }
    }

}