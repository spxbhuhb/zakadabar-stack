/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.json

/**
 * Communication functions for records.
 *
 * @property  companion   Companion of the Action DTO this comm belongs to.
 */
@PublicApi
open class ActionComm(
    private val companion: ActionDtoCompanion<*>
) : ActionCommInterface {

    @PublicApi
    override suspend fun <REQUEST : Any, RESPONSE> action(request: REQUEST, requestSerializer: KSerializer<REQUEST>, responseSerializer: KSerializer<RESPONSE>): RESPONSE {

        val headers = Headers()

        headers.append("content-type", "application/json")

        val body = json.encodeToString(requestSerializer, request)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        val responsePromise = window.fetch("/api/${companion.namespace}/${request::class.simpleName}", requestInit)
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(responseSerializer, text)
    }

}