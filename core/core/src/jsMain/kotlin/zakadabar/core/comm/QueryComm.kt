/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig.Companion.merge
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.util.PublicApi
import zakadabar.core.util.encodeURIComponent

/**
 * Communication functions for records.
 *
 * @property  companion   Companion of the query DTO this comm belongs to.
 */
@PublicApi
open class QueryComm(
    val companion: QueryBoCompanion
) : CommBase(), QueryCommInterface {

    @PublicApi
    override suspend fun <RQ : Any, RS : Any?> queryOrNull(
        request: RQ,
        requestSerializer: KSerializer<RQ>,
        responseSerializer: KSerializer<RS>,
        executor: Executor?,
        config: CommConfig?
    ): RS? {

        val q = encodeURIComponent(json.encodeToString(requestSerializer, request))

        val url = merge("/query/${request::class.simpleName}?q=${q}", companion.boNamespace, config, companion.commConfig)

        val response = commBlock {
            val responsePromise = window.fetch(url)
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
    override suspend fun <RQ : Any, RS : Any> query(
        request: RQ,
        requestSerializer: KSerializer<RQ>,
        responseSerializer: KSerializer<RS>,
        executor: Executor?,
        config: CommConfig?
    ): RS {
        return queryOrNull(request, requestSerializer, responseSerializer, executor, config) ?: throw NoSuchElementException()
    }


}