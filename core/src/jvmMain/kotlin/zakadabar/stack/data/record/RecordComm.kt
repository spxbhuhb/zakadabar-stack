/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.util.PublicApi

/**
 * REST communication functions for objects that implement [RecordDto]
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
        require(dto.id.isEmpty()) { "id is not 0 in $dto" }

        val text = client.post<String>("$baseUrl/api/$namespace/record") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(serializer, dto)
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun read(id: RecordId<T>): T {
        val text = client.get<String>("$baseUrl/api/$namespace/record/$id")

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun update(dto: T): T {
        require(! dto.id.isEmpty()) { "ID of the $dto is 0 " }

        val text = client.patch<String>("$baseUrl/api/$namespace/record/${dto.id}") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(serializer, dto)
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun all(): List<T> {
        val text = client.get<String>("$baseUrl/api/$namespace/record")

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: RecordId<T>) {
        client.delete<Unit>("$baseUrl/api/$namespace/record/$id")
    }

    @PublicApi
    override fun blobCreate(
        dataRecordId: RecordId<T>?, name: String, type: String, data: Any,
        callback: (dto: BlobDto, state: BlobCreateState, uploaded: Long) -> Unit
    ) {
        require(data is ByteArray) // TODO should use an interface instead

        @Suppress("UNCHECKED_CAST") // can't do much here
        val dto = BlobDto(EmptyRecordId(), dataRecordId as? RecordId<DtoBase>, namespace, name, type, data.size.toLong())

        callback(dto, BlobCreateState.Starting, 0)

        GlobalScope.launch(Dispatchers.Default) {

            try {
                val text = client.post<String>("$baseUrl/api/$namespace/blob${if (dataRecordId == null) "" else "/$dataRecordId"}") {
                    header("Content-Type", type)
                    header("Content-Disposition", """attachment; filename="$name"""")
                    body = ByteArrayContent(data)
                }

                callback(Json.decodeFromString(BlobDto.serializer(), text), BlobCreateState.Done, data.size.toLong())

            } catch (ex: Exception) {
                ex.printStackTrace() // TODO replace this with a function similar to writeLog (from Android project)
                callback(dto, BlobCreateState.Error, 0L)
            }
        }
    }

    @PublicApi
    suspend fun blobCreate(dataRecordId: RecordId<T>?, name: String, type: ContentType, data: ByteArray): BlobDto {
        val text = client.post<String>("$baseUrl/api/$namespace/blob${if (dataRecordId == null) "" else "/$dataRecordId"}") {
            header("Content-Disposition", """attachment; filename="$name"""")
            body = ByteArrayContent(data, contentType = type)
        }

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    suspend fun blobRead(blobId: RecordId<BlobDto>): ByteArray {
        return client.get("$baseUrl/api/$namespace/blob/content/$blobId")
    }

    @PublicApi
    override suspend fun blobMetaList(dataRecordId: RecordId<T>): List<BlobDto> {

        val text = client.get<String>("$baseUrl/api/$namespace/blob/list/$dataRecordId")

        return Json.decodeFromString(ListSerializer(BlobDto.serializer()), text)
    }

    @PublicApi
    override suspend fun blobMetaRead(blobId: RecordId<BlobDto>): BlobDto {

        val text = client.get<String>("$baseUrl/api/$namespace/blob/meta/$blobId")

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    override suspend fun blobMetaUpdate(dto: BlobDto): BlobDto {
        require(! dto.id.isEmpty()) { "ID of the $dto is 0 " }

        val text = client.patch<String>("$baseUrl/api/$namespace/blob/meta/${dto.id}") {
            header("Content-Type", "application/json")
            body = Json.encodeToString(BlobDto.serializer(), dto)
        }

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    override suspend fun blobDelete(blobId: RecordId<BlobDto>) {
        client.delete<Unit>("$baseUrl/api/$namespace/blob/$blobId")
    }

}