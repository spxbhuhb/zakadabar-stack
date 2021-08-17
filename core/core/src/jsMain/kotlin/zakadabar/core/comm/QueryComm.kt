/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.browser.util.encodeURIComponent
import zakadabar.core.util.PublicApi

/**
 * Communication functions for records.
 *
 * @property  companion   Companion of the query DTO this comm belongs to.
 */
@PublicApi
open class QueryComm(
    private val companion: QueryBoCompanion
) : CommBase(), QueryCommInterface {

    @PublicApi
    override suspend fun <RQ : Any, RS : Any?> queryOrNull(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<RS>): RS? {

        val q = encodeURIComponent(Json.encodeToString(requestSerializer, request))

        val response = commBlock {
            val responsePromise = window.fetch("/api/${companion.boNamespace}/query/${request::class.simpleName}?q=${q}")
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
    override suspend fun <RQ : Any, RS : Any> query(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<RS>): RS {
        return queryOrNull(request, requestSerializer, responseSerializer) ?: throw NoSuchElementException()
    }


}