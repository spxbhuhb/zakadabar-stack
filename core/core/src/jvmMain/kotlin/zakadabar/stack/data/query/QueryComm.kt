/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.data.CommBase.Companion.baseUrl
import zakadabar.stack.data.CommBase.Companion.client
import zakadabar.stack.util.PublicApi

/**
 * Communication functions for query DTOs.
 *
 * @property  companion   Companion of the query DTO this comm belongs to.
 */
@PublicApi
open class QueryComm(
    private val companion: QueryBoCompanion
) : QueryCommInterface {

    override suspend fun <RQ : Any, RS : Any?> queryOrNull(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<RS>): RS? {
        val q = Json.encodeToString(requestSerializer, request).encodeURLPath()

        val text = client.get<String>("${baseUrl}/api/${companion.boNamespace}/query/${request::class.simpleName}?q=${q}")

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