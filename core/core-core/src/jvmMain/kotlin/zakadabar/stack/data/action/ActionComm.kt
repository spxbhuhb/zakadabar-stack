/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import io.ktor.client.request.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.data.CommBase.Companion.baseUrl
import zakadabar.stack.data.CommBase.Companion.client
import zakadabar.stack.util.PublicApi

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
    override suspend fun <REQUEST : Any, RESPONSE> action(request: REQUEST, requestSerializer: KSerializer<REQUEST>, responseSerializer: KSerializer<RESPONSE>): RESPONSE {
        val text = client.post<String>("${baseUrl}/api/${companion.boNamespace}/action/${request::class.simpleName}") {
            header("Content-Type", "application/json; charset=UTF-8")
            body = Json.encodeToString(requestSerializer, request)
        }

        return Json.decodeFromString(responseSerializer, text)
    }

}