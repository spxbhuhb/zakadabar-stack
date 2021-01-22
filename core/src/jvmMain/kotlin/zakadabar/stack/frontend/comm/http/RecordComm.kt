/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.http

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.json

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

    companion object {
        lateinit var baseUrl: String

        val client = HttpClient()
    }

    /**
     * Creates a new object on the server.
     *
     * @param  dto  DTO of the object to create.
     */
    override suspend fun create(dto: T): T {
        require(dto.id == 0L) { "id is not 0 in $dto" }

        val text = client.post<String>("$baseUrl/api/$recordType") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(serializer, dto)
        }

        return Json.decodeFromString(serializer, text)
    }

    /**
     * Fetches an object.
     *
     * @param  id  Id of the object.
     *
     * @return  The DTO fetched.
     */
    @PublicApi
    override suspend fun read(id: Long): T {
        val text = client.get<String>("$baseUrl/api/$recordType/$id")

        return Json.decodeFromString(serializer, text)
    }

    /**
     * Updates an object on the server.
     *
     * @param  dto  DTO of the object to update.
     */
    override suspend fun update(dto: T): T {
        require(dto.id != 0L) { "ID of the $dto is 0 " }

        val text = client.patch<String>("$baseUrl/api/$recordType/${dto.id}") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(serializer, dto)
        }

        return Json.decodeFromString(serializer, text)
    }

    /**
     * Retrieves all objects.
     *
     * @return  The response send by the server.
     */
    @PublicApi
    override suspend fun all(): List<T> {
        val text = client.get<String>("$baseUrl/api/$recordType")

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    /**
     * Deletes an object.
     *
     * @param  id  Id of the object to delete.
     */
    @PublicApi
    override suspend fun delete(id: Long) {
        client.delete<Unit>("$baseUrl/api/$recordType/$id")
    }

    /**
     * Searches for objects by the passed search parameters.
     *
     * @param   request            The query request to send.
     * @param   requestSerializer  Serializer for the request.
     *
     * @return  List of objects found.
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
     */
    @PublicApi
    override suspend fun <RQ : Any, RS> query(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<List<RS>>): List<RS> {

        val q = Json.encodeToString(requestSerializer, request).encodeURLPath()

        val text = client.get<String>("$baseUrl/api/$recordType/${request::class.simpleName}?q=${q}")

        return Json.decodeFromString(responseSerializer, text)
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
     * @param  data      BLOB data, a ByteArray (cannot use ByteArray because of JavaScrip)
     * @param  callback  Callback function to report progress, completion or error.
     *
     * @return A DTO which contains data of the blob. The `id` is 0 in this DTO.
     */
    override fun blobCreate(
        dataRecordId: Long?, name: String, type: String,
        data: Any,
        callback: (dto: BlobDto, state: BlobCreateState, uploaded: Long) -> Unit
    ) {
        require(data is ByteArray) // TODO should use an interface instead

        val dto = BlobDto(0L, dataRecordId, recordType, name, type, data.size.toLong())

        callback(dto, BlobCreateState.Starting, 0)

        GlobalScope.launch(Dispatchers.Default) {

            try {
                val text = client.post<String>("$baseUrl/api/$recordType/$dataRecordId/blob") {
                    header("Content-Type", type)
                    header("Content-Disposition", """attachment; filename="$name"""")
                    body = ByteArrayContent(data)
                }

                callback(json.decodeFromString(BlobDto.serializer(), text), BlobCreateState.Done, data.size.toLong())

            } catch (ex: Exception) {
                ex.printStackTrace() // TODO replace this with a function similar to writeLog (from Android project)
                callback(dto, BlobCreateState.Error, 0L)
            }
        }
    }

    /**
     * Retrieves metadata of BLOBs.
     *
     * @return  List of BLOB metadata.
     */
    @PublicApi
    override suspend fun blobMetaRead(dataRecordId: Long): List<BlobDto> {

        val text = client.get<String>("$baseUrl/api/$recordType/$dataRecordId/blob/all")

        return Json.decodeFromString(ListSerializer(BlobDto.serializer()), text)
    }

    /**
     * Updates an metadata of a blob: recordId, name, type fields.
     *
     * @param  dto  DTO of the object to update.
     */
    override suspend fun blobMetaUpdate(dto: BlobDto): BlobDto {
        require(dto.id != 0L) { "ID of the $dto is 0 " }

        val text = client.patch<String>("$baseUrl/api/$recordType/${dto.dataRecord}/blob") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(BlobDto.serializer(), dto)
        }

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    /**
     * Deletes a BLOB.
     *
     * @param  dataRecordId  Id of the data record the blob to delete belongs to.
     * @param  blobId        Id of the blob to delete.
     */
    @PublicApi
    override suspend fun blobDelete(dataRecordId: Long, blobId: Long) {
        client.delete<Unit>("$baseUrl/api/$recordType/$dataRecordId/blob/$blobId")
    }
}