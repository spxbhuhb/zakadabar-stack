/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import io.ktor.client.*
import io.ktor.client.features.cookies.*
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
) : RecordCommInterface<T> {

    companion object {
        lateinit var baseUrl: String

        val client = HttpClient {
            install(HttpCookies) {
                // Will keep an in-memory map with all the cookies from previous requests.
                storage = AcceptAllCookiesStorage()
            }
        }
    }

    @PublicApi
    override suspend fun create(dto: T): T {
        require(dto.id == 0L) { "id is not 0 in $dto" }

        val text = client.post<String>("$baseUrl/api/$recordType") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(serializer, dto)
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun read(id: Long): T {
        val text = client.get<String>("$baseUrl/api/$recordType/$id")

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun update(dto: T): T {
        require(dto.id != 0L) { "ID of the $dto is 0 " }

        val text = client.patch<String>("$baseUrl/api/$recordType") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(serializer, dto)
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun all(): List<T> {
        val text = client.get<String>("$baseUrl/api/$recordType")

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: Long) {
        client.delete<Unit>("$baseUrl/api/$recordType/$id")
    }

    @PublicApi
    override fun blobCreate(
        dataRecordId: Long?, name: String, type: String, data: Any,
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

    @PublicApi
    override suspend fun blobCreate(dataRecordId: Long?, name: String, type: ContentType, data: ByteArray): BlobDto {
        val text = client.post<String>("$baseUrl/api/$recordType/$dataRecordId/blob") {
            header("Content-Disposition", """attachment; filename="$name"""")
            body = ByteArrayContent(data, contentType = type)
        }

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    suspend fun blobRead(dataRecordId: Long, blobId: Long): ByteArray {
        return client.get("$baseUrl/api/$recordType/$dataRecordId/blob/$blobId")
    }

    @PublicApi
    override suspend fun blobMetaRead(dataRecordId: Long): List<BlobDto> {

        val text = client.get<String>("$baseUrl/api/$recordType/$dataRecordId/blob/all")

        return Json.decodeFromString(ListSerializer(BlobDto.serializer()), text)
    }

    @PublicApi
    override suspend fun blobMetaUpdate(dto: BlobDto): BlobDto {
        require(dto.id != 0L) { "ID of the $dto is 0 " }

        val text = client.patch<String>("$baseUrl/api/$recordType/${dto.dataRecord}/blob") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(BlobDto.serializer(), dto)
        }

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    override suspend fun blobDelete(dataRecordId: Long, blobId: Long) {
        client.delete<Unit>("$baseUrl/api/$recordType/$dataRecordId/blob/$blobId")
    }

}