/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.serializer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.backend.route.Router
import zakadabar.stack.backend.server
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

open class KtorRouter<T : BaseBo>(
    @PublicApi
    protected val businessLogic: BusinessLogicCommon<out BaseBo>
) : Router<T> {

    private val actionClassList = mutableListOf<Pair<KClass<out BaseBo>, (Executor, BaseBo) -> Any?>>()

    private val queryClassList = mutableListOf<Pair<KClass<out BaseBo>, (Executor, BaseBo) -> Any?>>()

    override var qualifier = "entity"

    /**
     * Adds cache control directive to GET requests, except BLOB content.
     * Default implementation calls the function with the same name from the
     * server. The function in the server sets the Cache-Control header to the
     * value specified by the server settings. Default is "no-cache, no-store".
     */
    open fun apiCacheControl(call: ApplicationCall) {
        server.apiCacheControl(call)
    }

    override fun <RQ : ActionBo<RS>, RS : Any?> action(actionClass: KClass<RQ>, actionFunc: (Executor, RQ) -> RS) {
        @Suppress("UNCHECKED_CAST") // the parameter setup above ensures consistency
        actionClassList += (actionClass to actionFunc) as (Pair<KClass<out BaseBo>, (Executor, BaseBo) -> Any?>)
    }

    override fun <RQ : QueryBo<RS>, RS : Any?> query(queryClass: KClass<RQ>, queryFunc: (Executor, RQ) -> RS) {
        @Suppress("UNCHECKED_CAST") // the parameter setup above ensures consistency
        queryClassList += (queryClass to queryFunc) as Pair<KClass<out BaseBo>, (Executor, BaseBo) -> Any?>
    }

    override fun installRoutes(context: Any) {
        with(context as Route) {

            actionClassList.forEach {
                val (boClass, func) = it
                post("${businessLogic.namespace}/action/${boClass.simpleName}") {
                    action(call, boClass, func)
                }
            }

            queryClassList.forEach {
                val (boClass, func) = it
                get("${businessLogic.namespace}/query/${boClass.simpleName}") {
                    query(call, boClass, func)
                }
            }
        }
    }

    open suspend fun action(call: ApplicationCall, actionClass: KClass<out BaseBo>, actionFunc: (Executor, BaseBo) -> Any?) {
        val executor = call.executor()
        val aText = call.receive<String>()
        val aObj = Json.decodeFromString(serializer(actionClass.createType()), aText) as BaseBo

        @Suppress("UNCHECKED_CAST")
        val response = businessLogic.actionWrapper(executor, actionFunc, aObj)

        if (response == null) {
            call.respond(JsonNull)
        } else {
            call.respond(response)
        }
    }

    override fun prepareAction(
        actionType : String,
        actionData : String
    ) : Pair<(Executor, BaseBo) -> Any?,BaseBo> {

        val (actionClass, actionFunc) = actionClassList.firstOrNull { it.first.simpleName == actionType }
            ?: throw NotImplementedError("no action found for action type '$actionType'")

        val aObj = Json.decodeFromString(serializer(actionClass.createType()), actionData) as BaseBo

        return actionFunc to aObj
    }

    private suspend fun query(call: ApplicationCall, queryClass: KClass<out BaseBo>, queryFunc: (Executor, BaseBo) -> Any?) {
        val executor = call.executor()

        apiCacheControl(call)

        val qText = call.parameters["q"]
        requireNotNull(qText)
        val qObj = Json.decodeFromString(serializer(queryClass.createType()), qText)

        @Suppress("UNCHECKED_CAST")
        val response = businessLogic.queryWrapper(executor, queryFunc, qObj as BaseBo)

        if (response == null) {
            call.respond(JsonNull)
        } else {
            call.respond(response)
        }
    }


}