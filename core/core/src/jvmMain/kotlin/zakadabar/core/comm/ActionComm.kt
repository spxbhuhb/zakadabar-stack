/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommBase.Companion.client
import zakadabar.core.comm.CommConfig.Companion.localCommonBl
import zakadabar.core.comm.CommConfig.Companion.merge
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.data.BaseBo
import zakadabar.core.util.PublicApi

/**
 * Communication functions for Action DTOs.
 *
 * @property  companion   Companion of the Action DTO this comm belongs to.
 */
@PublicApi
open class ActionComm(
    val companion: ActionBoCompanion
) : ActionCommInterface {

    @PublicApi
    override suspend fun <REQUEST : Any, RESPONSE> actionOrNull(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        executor: Executor?,
        config: CommConfig?
    ): RESPONSE? {

        localCommonBl(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }

            val func =  it.router.funcForAction(request as BaseBo)

            @Suppress("UNCHECKED_CAST") // router register methods should ensure that this is right
            return it.actionWrapper(executor, func, request) as RESPONSE?
        }

        val path = merge("/action/${request::class.simpleName}", companion.boNamespace, config, companion.commConfig)

        val text = client.post {
            url(path)
            header("Content-Type", "application/json; charset=UTF-8")
            setBody(Json.encodeToString(requestSerializer, request))
        }.bodyAsText()

        return if (text == "null") {
            null
        } else {
            Json.decodeFromString(responseSerializer, text)
        }
    }

    @PublicApi
    override suspend fun <REQUEST : Any, RESPONSE> action(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        executor: Executor?,
        config: CommConfig?
    ): RESPONSE {
        return actionOrNull(request, requestSerializer, responseSerializer, executor, config)!!
    }

}