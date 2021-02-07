/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.action

import io.ktor.application.*
import io.ktor.request.*
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

interface ActionBackend : BackendModule {

    val recordType: String

    val logger: Logger

    /**
     * Adds an Action route for this backend.
     */
    fun <RQ : Any, RS : Any> Route.action(actionDto: KClass<RQ>, func: (Executor, RQ) -> RS) {
        post("$recordType/${actionDto.simpleName}") {

            val executor = call.executor()
            val aText = call.receive<String>()
            val aObj = Json.decodeFromString(serializer(actionDto.createType()), aText)

            if (Server.logReads) logger.info("${executor.accountId}: ACTION ${actionDto.simpleName} $aText")

            @Suppress("UNCHECKED_CAST")
            call.respond(func(executor, aObj as RQ))
        }
    }

    /**
     * Adds an Action route for this backend.
     */
    fun <RQ : Any, RS : Any> Route.action(actionDto: KClass<RQ>, func: (ApplicationCall, Executor, RQ) -> RS) {
        post("$recordType/${actionDto.simpleName}") {

            val executor = call.executor()
            val aText = call.receive<String>()
            val aObj = Json.decodeFromString(serializer(actionDto.createType()), aText)

            if (Server.logReads) logger.info("${executor.accountId}: ACTION ${actionDto.simpleName} $aText")

            @Suppress("UNCHECKED_CAST")
            call.respond(func(call, executor, aObj as RQ))
        }
    }
}