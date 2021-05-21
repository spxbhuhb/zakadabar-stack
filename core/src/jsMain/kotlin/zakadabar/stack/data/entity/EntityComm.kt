/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.files.Blob
import org.w3c.xhr.ProgressEvent
import org.w3c.xhr.XMLHttpRequest
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.CommBase
import zakadabar.stack.data.builtin.BlobBo
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.PublicApi

/**
 * Communication functions for entities.
 *
 * @property  namespace    Namespace of the entity this comm handles.
 *
 * @property  serializer   The serializer to serialize/deserialize objects
 *                         sent/received.
 */
@PublicApi
open class EntityComm<T : EntityBo<T>>(
    private val namespace: String,
    private val serializer: KSerializer<T>
) : CommBase(), EntityCommInterface<T> {

    override suspend fun create(bo: T): T {
        if (! bo.id.isEmpty()) throw RuntimeException("id is not empty in $bo")

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = Json.encodeToString(serializer, bo)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        return sendAndReceive("/api/$namespace/entity", requestInit)
    }

    @PublicApi
    override suspend fun read(id: EntityId<T>): T {

        val headers = Headers()

        val requestInit = RequestInit(
            method = "GET",
            headers = headers,
        )

        return sendAndReceive("/api/$namespace/entity/$id", requestInit)
    }

    @PublicApi
    override suspend fun update(bo: T): T {
        if (bo.id.isEmpty()) throw RuntimeException("ID of the $bo is empty")

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = Json.encodeToString(serializer, bo)

        val requestInit = RequestInit(
            method = "PATCH",
            headers = headers,
            body = body
        )

        return sendAndReceive("/api/$namespace/entity/${bo.id}", requestInit)
    }

    @PublicApi
    override suspend fun all(): List<T> {
        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/entity")
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: EntityId<T>) {
        commBlock {
            val responsePromise = window.fetch("/api/$namespace/entity/$id", RequestInit(method = "DELETE"))
            checkStatus(responsePromise.await())
        }
    }

    private suspend fun sendAndReceive(path: String, requestInit: RequestInit): T {
        val response = commBlock {
            val responsePromise = window.fetch(path, requestInit)
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override fun blobCreate(entityId: EntityId<T>?, name: String, type: String, data: Any, callback: (bo: BlobBo, state: BlobCreateState, uploaded: Long) -> Unit) {
        require(data is Blob)

        io {
            // this block is here so the UI will perform a re-login if needed
            commBlock { checkStatus(window.fetch("/api/health").await()) }

            val req = XMLHttpRequest()

            @Suppress("UNCHECKED_CAST") // T is DtoBase
            val bo = BlobBo(EntityId(), entityId as EntityId<BaseBo>?, namespace, name, type, data.size.toLong())

            req.addEventListener("progress", { callback(bo, BlobCreateState.Progress, (it as ProgressEvent).loaded.toLong()) })
            req.addEventListener("load", { callback(Json.decodeFromString(BlobBo.serializer(), req.responseText), BlobCreateState.Done, data.size.toLong()) })
            req.addEventListener("error", { callback(bo, BlobCreateState.Error, 0) })
            req.addEventListener("abort", { callback(bo, BlobCreateState.Abort, 0) })

            callback(bo, BlobCreateState.Starting, 0)

            // intent = true tells the backend that this is just an intent, we don't have a backend entity yet, so
            // the data entity id of the path is useless

            val url = "/api/$namespace/blob${if (entityId == null) "" else "/$entityId"}"

            req.open("POST", url, true)
            req.setRequestHeader("Content-Type", type)
            req.setRequestHeader("Content-Disposition", """attachment; filename="$name"""")
            req.send(data)
        }
    }


    @PublicApi
    override suspend fun blobMetaList(entityId: EntityId<T>): List<BlobBo> {
        require(! entityId.isEmpty()) { "entity id is empty" }

        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/list/$entityId")
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(ListSerializer(BlobBo.serializer()), text)
    }

    @PublicApi
    override suspend fun blobMetaRead(blobId: EntityId<BlobBo>): BlobBo {
        require(! blobId.isEmpty()) { "blob id is empty" }

        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/meta/$blobId")
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(BlobBo.serializer(), text)
    }

    @PublicApi
    override suspend fun blobMetaUpdate(bo: BlobBo): BlobBo {
        if (bo.id.isEmpty()) throw RuntimeException("ID of the $bo is 0 ")

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = Json.encodeToString(BlobBo.serializer(), bo)

        val requestInit = RequestInit(
            method = "PATCH",
            headers = headers,
            body = body
        )

        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/meta/${bo.id}", requestInit)
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(BlobBo.serializer(), text)
    }

    @PublicApi
    override suspend fun blobDelete(blobId: EntityId<BlobBo>) {
        commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/$blobId", RequestInit(method = "DELETE"))
            checkStatus(responsePromise.await())
        }
    }
}