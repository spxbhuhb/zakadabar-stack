/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig.Companion.merge
import zakadabar.core.data.ActionBoCompanion
import zakadabar.core.util.PublicApi

/**
 * Communication functions for records.
 *
 * @property  companion   Companion of the Action DTO this comm belongs to.
 */
@PublicApi
open class ActionComm(
    val companion: ActionBoCompanion
) : CommBase(), ActionCommInterface {

    @PublicApi
    override suspend fun <REQUEST : Any, RESPONSE> actionOrNull(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        executor: Executor?,
        config: CommConfig?
    ): RESPONSE? {

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = json.encodeToString(requestSerializer, request)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        val url = merge("/action/${request::class.simpleName}", companion.boNamespace, config, companion.commConfig)

        val response = commBlock {
            val responsePromise = window.fetch(url, requestInit)
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return if (text == "null") {
            null
        } else {
            json.decodeFromString(responseSerializer, text)
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