/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import zakadabar.stack.data.CommBase
import zakadabar.stack.util.PublicApi

/**
 * Communication functions for records.
 *
 * @property  companion   Companion of the Action DTO this comm belongs to.
 */
@PublicApi
open class ActionComm(
    private val companion: ActionDtoCompanion<*>
) : CommBase(), ActionCommInterface {

    @PublicApi
    override suspend fun <REQUEST : Any, RESPONSE> action(request: REQUEST, requestSerializer: KSerializer<REQUEST>, responseSerializer: KSerializer<RESPONSE>): RESPONSE {

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = Json.encodeToString(requestSerializer, request)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        val response = commBlock {
            val responsePromise = window.fetch("/api/${companion.namespace}/action/${request::class.simpleName}", requestInit)
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(responseSerializer, text)
    }

}