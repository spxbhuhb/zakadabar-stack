/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data.action

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import zakadabar.core.data.CommBase
import zakadabar.core.util.PublicApi

/**
 * Communication functions for records.
 *
 * @property  companion   Companion of the Action DTO this comm belongs to.
 */
@PublicApi
open class ActionComm(
    private val companion: ActionBoCompanion
) : CommBase(), ActionCommInterface {

    @PublicApi
    override suspend fun <REQUEST : Any, RESPONSE> actionOrNull(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        baseUrl : String?
    ): RESPONSE? {

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = Json.encodeToString(requestSerializer, request)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        val response = commBlock {
            val base = baseUrl?.trim('/') ?: "/api/${companion.boNamespace}"
            val url = "$base/action/${request::class.simpleName}"
            val responsePromise = window.fetch(url, requestInit)
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

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
        return actionOrNull(request, requestSerializer, responseSerializer)!!
    }

}