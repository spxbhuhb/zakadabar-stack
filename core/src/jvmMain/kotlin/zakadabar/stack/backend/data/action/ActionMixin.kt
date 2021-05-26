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
import zakadabar.stack.backend.audit.Auditor
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

interface ActionMixin {

    val namespace: String

    val auditor : Auditor<*>

    /**
     * Adds an Action route for this backend.
     */
    fun <RQ : ActionBo<RS>, RS : BaseBo> Route.action(actionClass: KClass<RQ>, func: (Executor, RQ) -> RS) {
        post("$namespace/action/${actionClass.simpleName}") {

            val executor = call.executor()
            val aText = call.receive<String>()
            val aObj = Json.decodeFromString(serializer(actionClass.createType()), aText)

            @Suppress("UNCHECKED_CAST")
            auditor.auditAction(executor, aObj as RQ)

            @Suppress("UNCHECKED_CAST")
            call.respond(func(executor, aObj) as Any)
        }
    }

    /**
     * Adds an Action route for this backend.
     */
    fun <RQ : ActionBo<RS>, RS : BaseBo> Route.action(actionBo: KClass<RQ>, func: (ApplicationCall, Executor, RQ) -> RS) {
        post("$namespace/action/${actionBo.simpleName}") {

            val executor = call.executor()
            val aText = call.receive<String>()
            val aObj = Json.decodeFromString(serializer(actionBo.createType()), aText)

            @Suppress("UNCHECKED_CAST")
            auditor.auditAction(executor, aObj as RQ)

            @Suppress("UNCHECKED_CAST")
            call.respond(func(call, executor, aObj) as Any)
        }
    }
}