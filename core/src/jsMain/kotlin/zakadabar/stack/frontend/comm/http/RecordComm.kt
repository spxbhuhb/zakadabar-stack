/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.http

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
import zakadabar.stack.comm.http.BlobCreateState
import zakadabar.stack.comm.http.Comm
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.frontend.errors.FetchError
import zakadabar.stack.frontend.errors.ensure
import zakadabar.stack.frontend.util.json
import zakadabar.stack.util.PublicApi

/**
 * REST communication functions for objects that implement [RecordDto]
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
) : Comm<T> {

    /**
     * Creates a new object on the server.
     *
     * @param  dto  DTO of the object to create.
     *
     * @throws  FetchError
     */
    override suspend fun create(dto: T): T {
        ensure(dto.id == 0L) { "id is not 0 in $dto" }

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

    /**
     * Fetches an object.
     *
     * @param  id  Id of the object.
     *
     * @return  The DTO fetched.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun read(id: Long): T {
        val responsePromise = window.fetch("/api/$recordType/$id")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(serializer, text)
    }

    /**
     * Updates an object on the server.
     *
     * @param  dto  DTO of the object to update.
     *
     * @throws  FetchError
     */
    override suspend fun update(dto: T): T {
        ensure(dto.id != 0L) { "ID of the $dto is 0 " }

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

    /**
     * Retrieves all objects.
     *
     * @return  The response send by the server.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun all(): List<T> {

        val responsePromise = window.fetch("/api/$recordType")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(ListSerializer(serializer), text)
    }

    /**
     * Deletes an object.
     *
     * @param  id  Id of the object to delete.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun delete(id: Long) {

        val responsePromise = window.fetch("/api/$recordType/$id", RequestInit(method = "DELETE"))
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }
    }

    /**
     * Searches for objects by the passed search parameters.
     *
     * @param   request            The query request to send.
     * @param   requestSerializer  Serializer for the request.
     *
     * @return  List of objects found.
     *
     * @throws  FetchError
     */
    override suspend fun <RQ : Any> query(request: RQ, requestSerializer: KSerializer<RQ>) =
        query(request, requestSerializer, ListSerializer(serializer))

    /**
     * Runs a generic query.
     *
     * @param   request  The query request to send.
     * @param   requestSerializer  Serializer for the request.
     * @param   responseSerializer   Serializer for the response.
     *
     * @return  The response send by the server.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun <RQ : Any, RS> query(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<List<RS>>): List<RS> {

        val responsePromise = window.fetch("/api/$recordType/${request::class.simpleName}?q=${Json.encodeToString(requestSerializer, request)}")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(responseSerializer, text)
    }

    private suspend fun sendAndReceive(path: String, requestInit: RequestInit): T {
        val responsePromise = window.fetch("/api/$path", requestInit)
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(serializer, text)
    }

    /**
     * Create a BLOB that belongs to the given record.
     *
     * Works only when the backend supports BLOBs for the record type.
     *
     * Calls [callback] once before the upload starts and then whenever
     * the state of the upload changes.
     *
     * Blob ID is 0 until the upload finishes. The actual id of the blob
     * is set when callback is called with state [BlobCreateState.Done].
     *
     * @param  dataRecordId  Id of the record the new BLOB belongs to.
     * @param  name      Name of the BLOB, typically the file name.
     * @param  type      Type of the BLOB, typically the MIME type.
     * @param  data      BLOB data, a Javascript [Blob].
     * @param  callback  Callback function to report progress, completion or error.
     *
     * @return A DTO which contains data of the blob. The `id` is 0 in this DTO.
     */
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

    /**
     * Retrieves metadata of BLOBs.
     *
     * @return  List of BLOB metadata.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun blobMetaRead(dataRecordId: Long): List<BlobDto> {

        val responsePromise = window.fetch("/api/$recordType/$dataRecordId/blob/all")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(ListSerializer(BlobDto.serializer()), text)
    }

    /**
     * Updates an metadata of a blob: recordId, name, type fields.
     *
     * @param  dto  DTO of the object to update.
     *
     * @throws  FetchError
     */
    override suspend fun blobMetaUpdate(dto: BlobDto): BlobDto {
        ensure(dto.id != 0L) { "ID of the $dto is 0 " }

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

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(BlobDto.serializer(), text)
    }

    /**
     * Deletes a BLOB.
     *
     * @param  dataRecordId  Id of the data record the blob to delete belongs to.
     * @param  blobId        Id of the blob to delete.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun blobDelete(dataRecordId: Long, blobId: Long) {

        val responsePromise = window.fetch("/api/$recordType/$dataRecordId/blob/$blobId", RequestInit(method = "DELETE"))
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }
    }
}