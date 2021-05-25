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

        /**
         * Called then the server responds with HTTP status 4xx and 5xx.
         */
        var onError: suspend (ex: Exception) -> Unit = { }
    }

    @PublicApi
    override suspend fun create(dto: T): T {
        require(dto.id.isEmpty()) { "id is not 0 in $dto" }

        val text = try {
            client.post<String>("$baseUrl/api/$namespace/record") {
                header("Content-Type", "application/json")
                body = Json.encodeToString(serializer, dto)
            }
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun read(id: RecordId<T>): T {
        val text = try {
            client.get<String>("$baseUrl/api/$namespace/record/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun update(dto: T): T {
        require(! dto.id.isEmpty()) { "ID of the $dto is 0 " }

        val text = try {
            client.patch<String>("$baseUrl/api/$namespace/record/${dto.id}") {
                header("Content-Type", "application/json")
                body = Json.encodeToString(serializer, dto)
            }
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun all(): List<T> {
        val text = try {
            client.get<String>("$baseUrl/api/$namespace/record")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: RecordId<T>) {
        try {
            client.delete<Unit>("$baseUrl/api/$namespace/record/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
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
                callback(dto, BlobCreateState.Error, 0L)
                onError(ex)
                throw ex
            }
        }
    }

    @PublicApi
    override suspend fun blobCreate(dataRecordId: RecordId<T>?, name: String, type: ContentType, data: ByteArray): BlobDto {
        val text = try {
            client.post<String>("$baseUrl/api/$namespace/blob${if (dataRecordId == null) "" else "/$dataRecordId"}") {
                header("Content-Disposition", """attachment; filename="$name"""")
                body = ByteArrayContent(data, contentType = type)
            }
        } catch (ex : Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    suspend fun blobRead(blobId: RecordId<BlobDto>): ByteArray {
        try {
            return client.get("$baseUrl/api/$namespace/blob/content/$blobId")
        } catch (ex : Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override suspend fun blobMetaList(dataRecordId: RecordId<T>): List<BlobDto> {

        val text = try {
            client.get<String>("$baseUrl/api/$namespace/blob/list/$dataRecordId")
        } catch (ex : Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(BlobDto.serializer()), text)
    }

    @PublicApi
    override suspend fun blobMetaRead(blobId: RecordId<BlobDto>): BlobDto {

        val text = try {
            client.get<String>("$baseUrl/api/$namespace/blob/meta/$blobId")
        } catch (ex : Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    override suspend fun blobMetaUpdate(dto: BlobDto): BlobDto {
        require(! dto.id.isEmpty()) { "ID of the $dto is 0 " }

        val text = try {
            client.patch<String>("$baseUrl/api/$namespace/blob/meta/${dto.id}") {
                header("Content-Type", "application/json")
                body = Json.encodeToString(BlobDto.serializer(), dto)
            }
        } catch (ex : Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(BlobDto.serializer(), text)
    }

    @PublicApi
    override suspend fun blobDelete(blobId: RecordId<BlobDto>) {
        try {
            client.delete<Unit>("$baseUrl/api/$namespace/blob/$blobId")
        } catch (ex : Exception) {
            onError(ex)
            throw ex
        }
    }

}