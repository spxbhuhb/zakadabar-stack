/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.action.ActionDto
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

interface ActionBackend : BackendModule {

    val namespace: String

    val logger: Logger

    var logActions: Boolean

    /**
     * Adds an Action route for this backend.
     */
    fun <RQ : ActionDto<RS>, RS : DtoBase> Route.action(actionDto: KClass<RQ>, func: (Executor, RQ) -> RS) {
        post("$namespace/action/${actionDto.simpleName}") {

            val executor = call.executor()
            val aText = call.receive<String>()
            val aObj = Json.decodeFromString(serializer(actionDto.createType()), aText)

            if (logActions) logger.info("${executor.accountId}: ACTION ${actionDto.simpleName} $aText")

            @Suppress("UNCHECKED_CAST")
            call.respond(func(executor, aObj as RQ) as Any)
        }
    }

    /**
     * Adds an Action route for this backend.
     */
    fun <RQ : ActionDto<RS>, RS : DtoBase> Route.action(actionDto: KClass<RQ>, func: (ApplicationCall, Executor, RQ) -> RS) {
        post("$namespace/action/${actionDto.simpleName}") {

            val executor = call.executor()
            val aText = call.receive<String>()
            val aObj = Json.decodeFromString(serializer(actionDto.createType()), aText)

            if (logActions) logger.info("${executor.accountId}: ACTION ${actionDto.simpleName} $aText")

            @Suppress("UNCHECKED_CAST")
            call.respond(func(call, executor, aObj as RQ) as Any)
        }
    }
}