/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.BackendContext
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.createType

abstract class DtoBackend<T : RecordDto<T>> : BackendModule {

    abstract val dtoClass: KClass<T>

    private val logger = LoggerFactory.getLogger("DTO") !!

    open val dtoType
        get() = (dtoClass.companionObject !!.objectInstance as RecordDtoCompanion<*>).recordType

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
     * An initialization function that is called during system startup to
     * initialize this extension.
     *
     * When called all modules are loaded and the DB is accessible.
     */
    override fun init() = Unit

    /**
     * A cleanup function that is called during system shutdown to clean up
     * this extension. DB is still accessible at this point.
     */
    override fun cleanup() = Unit

    fun Route.crud() {
        route(dtoType) {

            post {
                val executor = call.executor()
                val request = call.receive(dtoClass)
                logger.info("${executor.entityId}: CREATE $dtoType $request")
                call.respond(create(executor, request))
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: "all"
                val executor = call.executor()

                if (BackendContext.logReads) logger.info("${executor.entityId}: READ $dtoType $id")

                if (id == "all") {
                    call.respond(all(executor))
                } else {
                    call.respond(read(executor, id as Long))
                }
            }

            patch {
                val executor = call.executor()
                val request = call.receive(dtoClass)
                logger.info("${executor.entityId}: UPDATE $dtoType $request")
                call.respond(update(executor, request))
            }

            delete {
                val executor = call.executor()
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException("missing id")
                logger.info("${executor.entityId}: DELETE $dtoType $id")
                call.respond(delete(executor, id))
            }
        }
    }

    fun <RQ : Any, RS : Any> Route.query(queryDto: KClass<RQ>, func: (Executor, RQ) -> RS) {
        get("$dtoType/${queryDto.simpleName}") {

            val executor = call.executor()

            val qText = call.parameters["q"]
            requireNotNull(qText)
            val qObj = Json.decodeFromString(serializer(queryDto.createType()), qText)

            if (BackendContext.logReads) logger.info("${executor.entityId}: GET $dtoType/${queryDto.simpleName} $qText")

            @Suppress("UNCHECKED_CAST")
            call.respond(func(executor, qObj as RQ))
        }
    }

}