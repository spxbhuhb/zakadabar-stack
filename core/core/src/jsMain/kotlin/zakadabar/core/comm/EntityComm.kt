/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig.Companion.merge
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.util.PublicApi

/**
 * Communication functions for entities.
 *
 * @property  companion    The companion object for this entity BO.
 *
 * @property  serializer   The serializer to serialize/deserialize objects
 *                         sent/received.
 */
@PublicApi
open class EntityComm<T : EntityBo<T>>(
    val companion: EntityBoCompanion<T>,
    val serializer: KSerializer<T>
) : CommBase(), EntityCommInterface<T> {

    override suspend fun create(bo: T, executor: Executor?, config: CommConfig?): T {
        if (! bo.id.isEmpty()) throw RuntimeException("id is not empty in $bo")

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = json.encodeToString(serializer, bo)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        return sendAndReceive(config, "/entity", requestInit)
    }

    @PublicApi
    override suspend fun read(id: EntityId<T>, executor: Executor?, config: CommConfig?): T {

        val headers = Headers()

        val requestInit = RequestInit(
            method = "GET",
            headers = headers,
        )

        return sendAndReceive(config, "/entity/$id", requestInit)
    }

    @PublicApi
    override suspend fun update(bo: T, executor: Executor?, config: CommConfig?): T {
        if (bo.id.isEmpty()) throw RuntimeException("ID of the $bo is empty")

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = json.encodeToString(serializer, bo)

        val requestInit = RequestInit(
            method = "PATCH",
            headers = headers,
            body = body
        )

        return sendAndReceive(config, "/entity/${bo.id}", requestInit)
    }

    @PublicApi
    override suspend fun all(executor: Executor?, config: CommConfig?): List<T> {

        val url = merge("/entity", companion.boNamespace, config, companion.commConfig)

        val response = commBlock {
            val responsePromise = window.fetch(url)
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: EntityId<T>, executor: Executor?, config: CommConfig?) {

        val url = merge("/entity/$id", companion.boNamespace, config, companion.commConfig)

        commBlock {
            val responsePromise = window.fetch(url, RequestInit(method = "DELETE"))
            checkStatus(responsePromise.await())
        }
    }

    protected suspend fun sendAndReceive(config: CommConfig?, path: String, requestInit: RequestInit): T {

        val url = merge(path, companion.boNamespace, config, companion.commConfig)

        val response = commBlock {
            val responsePromise = window.fetch(url, requestInit)
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(serializer, text)
    }

}