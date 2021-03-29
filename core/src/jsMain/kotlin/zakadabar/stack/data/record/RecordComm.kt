/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.files.Blob
import org.w3c.xhr.ProgressEvent
import org.w3c.xhr.XMLHttpRequest
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.json

/**
 * Communication functions for records.
 *
 * @property  recordType   Type of the record this comm handles.
 *
 * @property  serializer   The serializer to serialize/deserialize objects
 *                         sent/received.
 */
@PublicApi
open class RecordComm<T : RecordDto<T>>(
    private val recordType: String,
    private val serializer: KSerializer<T>
) : RecordCommInterface<T> {

    override suspend fun create(dto: T): T {
        if (dto.id != 0L) throw RuntimeException("id is not 0 in $dto")

        val headers = Headers()

        headers.append("content-type", "application/json")

        val body = json.encodeToString(serializer, dto)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        return sendAndReceive(recordType, requestInit)
    }

    @PublicApi
    override suspend fun read(id: Long): T {
        val responsePromise = window.fetch("/api/$recordType/$id")
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun update(dto: T): T {
        if (dto.id == 0L) throw RuntimeException("ID of the $dto is 0 ")

        val headers = Headers()

        headers.append("content-type", "application/json")

        val body = json.encodeToString(serializer, dto)

        val requestInit = RequestInit(
            method = "PATCH",
            headers = headers,
            body = body
        )

        return sendAndReceive(recordType, requestInit)
    }

    @PublicApi
    override suspend fun all(): List<T> {

        val responsePromise = window.fetch("/api/$recordType")
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: Long) {

        val responsePromise = window.fetch("/api/$recordType/$id", RequestInit(method = "DELETE"))
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()
    }

    private suspend fun sendAndReceive(path: String, requestInit: RequestInit): T {
        val responsePromise = window.fetch("/api/$path", requestInit)
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(serializer, text)
    }

    @PublicApi
    override fun blobCreate(
        dataRecordId: Long?, name: String, type: String,
        data: Any,
        callback: (dto: BlobDto, state: BlobCreateState, uploaded: Long) -> Unit
    ) {
        require(data is Blob)

        val req = XMLHttpRequest()

        val dto = BlobDto(0L, dataRecordId, recordType, name, type, data.size.toLong())

        req.addEventListener("progress", { callback(dto, BlobCreateState.Progress, (it as ProgressEvent).loaded.toLong()) })
        req.addEventListener("load", { callback(json.decodeFromString(BlobDto.serializer(), req.responseText), BlobCreateState.Done, data.size.toLong()) })
        req.addEventListener("error", { callback(dto, BlobCreateState.Error, 0) })
        req.addEventListener("abort", { callback(dto, BlobCreateState.Abort, 0) })

        callback(dto, BlobCreateState.Starting, 0)

        req.open("POST", "/api/$recordType/$dataRecordId/blob", true)
        req.setRequestHeader("Content-Type", type)
        req.setRequestHeader("Content-Disposition", """attachment; filename="$name"""")
        req.send(data)
    }

    @PublicApi
    override suspend fun blobCreate(dataRecordId: Long?, name: String, type: ContentType, data: ByteArray): BlobDto {
        TODO("Not yet implemented")
    }

    @PublicApi
    override suspend fun blobMetaRead(dataRecordId: Long): List<BlobDto> {

        val responsePromise = window.fetch("/api/$recordType/$dataRecordId/blob/all")
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(ListSerializer(BlobDto.serializer()), text)
    }

    @PublicApi
    override suspend fun blobMetaUpdate(dto: BlobDto): BlobDto {
        if (dto.id == 0L) throw RuntimeException("ID of the $dto is 0 ")

        val headers = Headers()

        headers.append("content-type", "application/json")

        val body = json.encodeToString(BlobDto.serializer(), dto)

        val requestInit = RequestInit(
            method = "PATCH",
            headers = headers,
            body = body
        )

        val responsePromise = window.fetch("/api/$recordType/${dto.dataRecord}/blob", requestInit)
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()
        if (! response.ok) throw RuntimeException()

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    override suspend fun blobDelete(dataRecordId: Long, blobId: Long) {

        val responsePromise = window.fetch("/api/$recordType/$dataRecordId/blob/$blobId", RequestInit(method = "DELETE"))
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()
    }
}