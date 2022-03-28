/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommBase.Companion.client
import zakadabar.core.comm.CommConfig.Companion.localCommonBl
import zakadabar.core.comm.CommConfig.Companion.merge
import zakadabar.core.data.BaseBo
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.util.PublicApi

/**
 * Communication functions for query DTOs.
 *
 * @property  companion   Companion of the query DTO this comm belongs to.
 */
@PublicApi
open class QueryComm(
    val companion: QueryBoCompanion
) : QueryCommInterface {

    override suspend fun <RQ : Any, RS : Any?> queryOrNull(
        request: RQ,
        requestSerializer: KSerializer<RQ>,
        responseSerializer: KSerializer<RS>,
        executor: Executor?,
        config : CommConfig?
    ): RS? {

        localCommonBl(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }

            val func =  it.router.funcForQuery(request as BaseBo)

            @Suppress("UNCHECKED_CAST") // router register methods should ensure that this is right
            return it.queryWrapper(executor, func, request) as RS?
        }

        val q = Json.encodeToString(requestSerializer, request).encodeURLPath()

        val url = merge("/query/${request::class.simpleName}?q=${q}", companion.boNamespace, config, companion.commConfig)

        val text = client.get<String>(url)

        return if (text == "null") {
            null
        } else {
            Json.decodeFromString(responseSerializer, text)
        }
    }

    @PublicApi
    override suspend fun <RQ : Any, RS : Any> query(
        request: RQ,
        requestSerializer: KSerializer<RQ>,
        responseSerializer: KSerializer<RS>,
        executor: Executor?,
        config : CommConfig?
    ): RS {
        return queryOrNull(request, requestSerializer, responseSerializer, executor, config) ?: throw NoSuchElementException()
    }

}