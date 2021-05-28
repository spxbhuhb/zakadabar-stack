/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.route.Router
import zakadabar.stack.backend.server
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

open class KtorRouter<T : EntityBo<T>>(
    private val businessLogic : EntityBusinessLogicBase<T>
) : Router<T> {

    private val actionClassList = mutableListOf<Pair<KClass<out BaseBo>, (Executor, BaseBo) -> BaseBo>>()

    private val queryClassList = mutableListOf<Pair<KClass<out BaseBo>, (Executor, BaseBo) -> Any>>()

    /**
     * Adds cache control directive to GET requests, except BLOB content.
     * Default implementation calls the function with the same name from the
     * server. The function in the server sets the Cache-Control header to the
     * value specified by the server settings. Default is "no-cache, no-store".
     */
    open fun apiCacheControl(call: ApplicationCall) {
        server.apiCacheControl(call)
    }

    override fun <RQ : ActionBo<RS>, RS : BaseBo> action(actionClass : KClass<RQ>, actionFunc : (Executor, RQ) -> RS) {
        @Suppress("UNCHECKED_CAST") // the parameter setup above ensures consistency
        actionClassList += (actionClass to actionFunc) as (Pair<KClass<out BaseBo>, (Executor, BaseBo) -> BaseBo>)
    }

    override fun <RQ : Any, RS : Any> query(queryClass: KClass<RQ>, queryFunc: (Executor, RQ) -> RS) {
        @Suppress("UNCHECKED_CAST") // the parameter setup above ensures consistency
        queryClassList += (queryClass to queryFunc) as Pair<KClass<out BaseBo>, (Executor, BaseBo) -> Any>
    }

    override fun installRoutes(context: Any) {
        with (context as Route) {
            route("${businessLogic.namespace}/entity") {

                get("/{rid?}") {
                    call.parameters["rid"]?.let { read(call, it) } ?: list(call)
                }

                post {
                    create(call)
                }

                patch("/{rid}") {
                    update(call)
                }

                delete("/{rid}") {
                    delete(call)
                }
            }

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

    suspend fun list(call: ApplicationCall) {

        val executor = call.executor()

        apiCacheControl(call)

        call.respond(businessLogic.listWrapper(executor) as Any)

    }

    open suspend fun read(call: ApplicationCall, id: String) {

        val executor = call.executor()

        apiCacheControl(call)

        call.respond(businessLogic.readWrapper(executor, EntityId<T>(id)) as Any)

    }

    suspend fun create(call: ApplicationCall) {

        val executor = call.executor()
        val request = call.receive(businessLogic.boClass)

        call.respond(businessLogic.createWrapper(executor, request) as Any)

    }

    suspend fun update(call: ApplicationCall) {

        val executor = call.executor()
        val request = call.receive(businessLogic.boClass)

        call.respond(businessLogic.updateWrapper(executor, request) as Any)

    }

    suspend fun delete(call: ApplicationCall) {

        val id = call.parameters["rid"] ?: throw BadRequestException("missing id")
        val executor = call.executor()

        businessLogic.deleteWrapper(executor, EntityId<T>(id))

        call.respond(HttpStatusCode.OK)

    }

    open suspend fun action(call: ApplicationCall, actionClass: KClass<out BaseBo>, actionFunc: (Executor, BaseBo) -> BaseBo) {
        val executor = call.executor()
        val aText = call.receive<String>()
        val aObj = Json.decodeFromString(serializer(actionClass.createType()), aText) as BaseBo

        @Suppress("UNCHECKED_CAST")
        val response = businessLogic.actionWrapper(executor, actionFunc, aObj)

        @Suppress("UNCHECKED_CAST")
        call.respond(response as Any)
    }

    private suspend fun query(call: ApplicationCall, queryClass: KClass<out BaseBo>, queryFunc: (Executor, BaseBo) -> Any) {
        val executor = call.executor()

        apiCacheControl(call)

        val qText = call.parameters["q"]
        requireNotNull(qText)
        val qObj = Json.decodeFromString(serializer(queryClass.createType()), qText)

        @Suppress("UNCHECKED_CAST")
        val response = businessLogic.queryWrapper(executor, queryFunc, qObj as BaseBo)

        @Suppress("UNCHECKED_CAST")
        call.respond(response)
    }


}