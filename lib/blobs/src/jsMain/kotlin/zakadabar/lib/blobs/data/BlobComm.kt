/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.khronos.webgl.Int8Array
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.files.Blob
import org.w3c.xhr.ProgressEvent
import org.w3c.xhr.XMLHttpRequest
import zakadabar.stack.data.CommBase
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.PublicApi

/**
 * Communication functions for blobs.
 *
 * @property  namespace    Namespace of the entity this comm handles.
 *
 */
@PublicApi
open class BlobComm<T : BlobBo<T,RT>, RT : EntityBo<RT>>(
    private val namespace: String,
    private val serializer: KSerializer<T>
) : CommBase(), BlobCommInterface<T,RT> {

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

        return sendAndReceive("/api/$namespace/blob/meta", requestInit)
    }

    @PublicApi
    override suspend fun read(id: EntityId<T>): T {

        val headers = Headers()

        val requestInit = RequestInit(
            method = "GET",
            headers = headers,
        )

        return sendAndReceive("/api/$namespace/blob/meta/$id", requestInit)
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

        return sendAndReceive("/api/$namespace/blob/meta/${bo.id}", requestInit)
    }

    @PublicApi
    override suspend fun all(): List<T> {
        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/meta")
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: EntityId<T>) {
        commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/meta/$id", RequestInit(method = "DELETE"))
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

    override suspend fun upload(bo: T, data: Any, callback: (bo: T, state: BlobCreateState, uploaded: Long) -> Unit) {
        require(data is Blob)

        val req = XMLHttpRequest()

        callback(bo, BlobCreateState.Starting, 0)

        req.addEventListener("progress", { callback(bo, BlobCreateState.Progress, (it as ProgressEvent).loaded.toLong()) })
        req.addEventListener("error", { callback(bo, BlobCreateState.Error, 0) })
        req.addEventListener("abort", { callback(bo, BlobCreateState.Abort, 0) })
        req.addEventListener("load", { callback(bo, BlobCreateState.Done, data.size.toLong()) })

        val url = "/api/$namespace/blob/content/${bo.id}"

        req.open("POST", url, true)
        req.setRequestHeader("Content-Type", bo.mimeType)
        req.setRequestHeader("Content-Disposition", """attachment; filename="${bo.name}"""")
        req.send(data)
    }

    override suspend fun download(id: EntityId<T>) : ByteArray {
        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/content/$id")
            checkStatus(responsePromise.await())
        }

        val ab = response.arrayBuffer().await()
        return Int8Array(ab).unsafeCast<ByteArray>()
    }

    override suspend fun listByReference(reference: EntityId<RT>, disposition : String?): List<T> {
        val q = disposition?.let { "?disposition=$it" } ?: ""
        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/list/$reference$q")
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(ListSerializer(serializer), text)
    }


}