/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import io.ktor.http.*
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
import zakadabar.stack.data.CommBase
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.PublicApi

/**
 * Communication functions for records.
 *
 * @property  namespace   Type of the record this comm handles.
 *
 * @property  serializer   The serializer to serialize/deserialize objects
 *                         sent/received.
 */
@PublicApi
open class RecordComm<T : RecordDto<T>>(
    private val namespace: String,
    private val serializer: KSerializer<T>
) : CommBase(), RecordCommInterface<T> {

    override suspend fun create(dto: T): T {
        if (! dto.id.isEmpty()) throw RuntimeException("id is not 0 in $dto")

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = Json.encodeToString(serializer, dto)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        return sendAndReceive("/api/$namespace/record", requestInit)
    }

    @PublicApi
    override suspend fun read(id: RecordId<T>): T {

        val headers = Headers()

        val requestInit = RequestInit(
            method = "GET",
            headers = headers,
        )

        return sendAndReceive("/api/$namespace/record/$id", requestInit)
    }

    @PublicApi
    override suspend fun update(dto: T): T {
        if (dto.id.isEmpty()) throw RuntimeException("ID of the $dto is 0 ")

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = Json.encodeToString(serializer, dto)

        val requestInit = RequestInit(
            method = "PATCH",
            headers = headers,
            body = body
        )

        return sendAndReceive("/api/$namespace/record/${dto.id}", requestInit)
    }

    @PublicApi
    override suspend fun all(): List<T> {
        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/record")
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: RecordId<T>) {
        commBlock {
            val responsePromise = window.fetch("/api/$namespace/record/$id", RequestInit(method = "DELETE"))
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
    override fun blobCreate(
        dataRecordId: RecordId<T>?, name: String, type: String,
        data: Any,
        callback: (dto: BlobDto, state: BlobCreateState, uploaded: Long) -> Unit
    ) {
        require(data is Blob)

        io {
            // this block is here so the UI will perform a re-login if needed
            commBlock { checkStatus(window.fetch("/api/health").await()) }

            val req = XMLHttpRequest()

            @Suppress("UNCHECKED_CAST") // T is DtoBase
            val dto = BlobDto(EmptyRecordId(), dataRecordId as RecordId<DtoBase>?, namespace, name, type, data.size.toLong())

            req.addEventListener("progress", { callback(dto, BlobCreateState.Progress, (it as ProgressEvent).loaded.toLong()) })
            req.addEventListener("load", { callback(Json.decodeFromString(BlobDto.serializer(), req.responseText), BlobCreateState.Done, data.size.toLong()) })
            req.addEventListener("error", { callback(dto, BlobCreateState.Error, 0) })
            req.addEventListener("abort", { callback(dto, BlobCreateState.Abort, 0) })

            callback(dto, BlobCreateState.Starting, 0)

            // intent = true tells the backend that this is just an intent, we don't have a backend record yet, so
            // the data record id of the path is useless

            // FIXME fix the clash between string record ids and "null"

            val url = "/api/$namespace/blob${if (dataRecordId == null) "" else "/$dataRecordId"}"

            req.open("POST", url, true)
            req.setRequestHeader("Content-Type", type)
            req.setRequestHeader("Content-Disposition", """attachment; filename="$name"""")
            req.send(data)
        }
    }

    @PublicApi
    override suspend fun blobCreate(dataRecordId: RecordId<T>?, name: String, type: ContentType, data: ByteArray): BlobDto {
        TODO("Not yet implemented")
    }

    @PublicApi
    override suspend fun blobMetaList(dataRecordId: RecordId<T>): List<BlobDto> {
        require(! dataRecordId.isEmpty()) { "data record id is empty" }

        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/list/$dataRecordId")
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(ListSerializer(BlobDto.serializer()), text)
    }

    @PublicApi
    override suspend fun blobMetaRead(blobId: RecordId<BlobDto>): BlobDto {
        require(! blobId.isEmpty()) { "blob id is empty" }

        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/meta/$blobId")
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    override suspend fun blobMetaUpdate(dto: BlobDto): BlobDto {
        if (dto.id.isEmpty()) throw RuntimeException("ID of the $dto is 0 ")

        val headers = Headers()

        headers.append("content-type", "application/json; charset=UTF-8")

        val body = Json.encodeToString(BlobDto.serializer(), dto)

        val requestInit = RequestInit(
            method = "PATCH",
            headers = headers,
            body = body
        )

        val response = commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/meta/${dto.id}", requestInit)
            checkStatus(responsePromise.await())
        }

        val textPromise = response.text()
        val text = textPromise.await()

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    override suspend fun blobDelete(blobId: RecordId<BlobDto>) {
        commBlock {
            val responsePromise = window.fetch("/api/$namespace/blob/$blobId", RequestInit(method = "DELETE"))
            checkStatus(responsePromise.await())
        }
    }
}