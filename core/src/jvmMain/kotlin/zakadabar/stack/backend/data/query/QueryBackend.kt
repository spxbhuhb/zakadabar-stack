/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.query

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.slf4j.Logger
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.util.executor
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

interface QueryBackend : BackendModule {

    val namespace: String

    val logger: Logger

    /**
     * Adds a Query route for this backend.
     */
    fun <RQ : Any, RS : Any> Route.query(queryDtoClass: KClass<RQ>, func: (Executor, RQ) -> RS) {
        get("$namespace/query/${queryDtoClass.simpleName}") {

            val executor = call.executor()

            val qText = call.parameters["q"]
            requireNotNull(qText)
            val qObj = Json.decodeFromString(serializer(queryDtoClass.createType()), qText)

            if (Server.logReads) logger.info("${executor.accountId}: GET ${queryDtoClass.simpleName} $qText")

            @Suppress("UNCHECKED_CAST")
            call.respond(func(executor, qObj as RQ))
        }
    }
}