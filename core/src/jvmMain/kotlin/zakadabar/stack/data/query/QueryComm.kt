/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.data.entity.EntityComm
import zakadabar.stack.util.PublicApi

/**
 * Communication functions for query DTOs.
 *
 * @property  companion   Companion of the query DTO this comm belongs to.
 */
@PublicApi
open class QueryComm(
    private val companion: QueryBoCompanion<*>
) : QueryCommInterface {

    @PublicApi
    override suspend fun <RQ : Any, RS> query(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<List<RS>>): List<RS> {

        val q = Json.encodeToString(requestSerializer, request).encodeURLPath()

        val text = EntityComm.client.get<String>("${EntityComm.baseUrl}/api/${companion.boNamespace}/query/${request::class.simpleName}?q=${q}")

        return Json.decodeFromString(responseSerializer, text)
    }

}