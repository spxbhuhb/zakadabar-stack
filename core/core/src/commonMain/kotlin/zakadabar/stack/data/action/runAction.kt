/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import io.ktor.client.request.*
import zakadabar.stack.data.CommBase

/**
 * Run an action without having the action class. Intended for framework use as this is
 * not type safe.
 *
 * @param  namespace   Namespace of the action (bo.boNamespace).
 * @param  type        Type of the action (bo::class.simpleName).
 * @param  data        Content of the action (Json.encodeToString(bo.serializer(), bo).
 *
 * @return Result of the execution. This method does not throw exceptions but catches all
 *         of them and puts them into [RunActionResult.exception]. Indicator of success
 *         is [RunActionResult.success].
 */
actual suspend fun runAction(namespace: String, type: String, data: String): String {
    return CommBase.client.post("${CommBase.baseUrl}/api/${namespace}/action/${type}") {
        header("Content-Type", "application/json; charset=UTF-8")
        body = data
    }
}