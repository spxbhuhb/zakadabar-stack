/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data.action

import io.ktor.client.request.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.core.data.CommBase
import zakadabar.core.data.CommBase.Companion.client
import zakadabar.core.util.PublicApi

/**
 * Communication functions for Action DTOs.
 *
 * @property  companion   Companion of the Action DTO this comm belongs to.
 */
@PublicApi
open class ActionComm(
    private val companion: ActionBoCompanion
) : ActionCommInterface {

    @PublicApi
    override suspend fun <REQUEST : Any, RESPONSE> actionOrNull(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        baseUrl : String?
    ): RESPONSE? {
        val base = baseUrl?.trim('/') ?: CommBase.baseUrl + "/api/${companion.boNamespace}"
        val url = "$base/action/${request::class.simpleName}"

        val text = client.post<String>(url) {
            header("Content-Type", "application/json; charset=UTF-8")
            body = Json.encodeToString(requestSerializer, request)
        }
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
        baseUrl : String?
    ): RESPONSE {
        return actionOrNull(request, requestSerializer, responseSerializer, baseUrl)!!
    }

}