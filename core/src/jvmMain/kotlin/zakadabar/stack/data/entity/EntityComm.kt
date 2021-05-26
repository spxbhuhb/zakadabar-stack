/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

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
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.BlobBo
import zakadabar.stack.util.PublicApi

/**
 * REST communication functions for entities.
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
) : EntityCommInterface<T> {

    companion object {
        lateinit var baseUrl: String

        val client = HttpClient {
            install(HttpCookies) {
                // Will keep an in-memory map with all the cookies from previous requests.
                storage = AcceptAllCookiesStorage()
            }
        }

        /**
         * Called then Ktor client throws an exception.
         */
        var onError: suspend (ex: Exception) -> Unit = { }
    }

    @PublicApi
    override suspend fun create(bo: T): T {
        require(bo.id.isEmpty()) { "id is empty in $bo" }

        val text = try {
            client.post<String>("$baseUrl/api/$namespace/entity") {
                header("Content-Type", "application/json")
                body = Json.encodeToString(serializer, bo)
            }
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun read(id: EntityId<T>): T {
        val text = try {
            client.get<String>("$baseUrl/api/$namespace/entity/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun update(bo: T): T {
        require(! bo.id.isEmpty()) { "ID of the $bo is 0 " }

        val text = try {
            client.patch<String>("$baseUrl/api/$namespace/entity/${bo.id}") {
                header("Content-Type", "application/json")
                body = Json.encodeToString(serializer, bo)
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
            client.get<String>("$baseUrl/api/$namespace/entity")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: EntityId<T>) {
        try {
            client.delete<Unit>("$baseUrl/api/$namespace/entity/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override fun blobCreate(entityId: EntityId<T>?, name: String, type: String, data: Any, callback: (bo: BlobBo, state: BlobCreateState, uploaded: Long) -> Unit) {
        require(data is ByteArray) // TODO should use an interface instead

        @Suppress("UNCHECKED_CAST") // can't do much here
        val dto = BlobBo(EntityId(), entityId as? EntityId<BaseBo>, namespace, name, type, data.size.toLong())

        callback(dto, BlobCreateState.Starting, 0)

        GlobalScope.launch(Dispatchers.Default) {

            try {
                val text = client.post<String>("$baseUrl/api/$namespace/blob${if (entityId == null) "" else "/$entityId"}") {
                    header("Content-Type", type)
                    header("Content-Disposition", """attachment; filename="$name"""")
                    body = ByteArrayContent(data)
                }

                callback(Json.decodeFromString(BlobBo.serializer(), text), BlobCreateState.Done, data.size.toLong())

            } catch (ex: Exception) {
                callback(dto, BlobCreateState.Error, 0L)
                onError(ex)
                throw ex
            }
        }
    }

    @PublicApi
    suspend fun blobCreate(dataEntityId: EntityId<T>?, name: String, type: ContentType, data: ByteArray): BlobBo {
        val text = try {
            client.post<String>("$baseUrl/api/$namespace/blob${if (dataEntityId == null) "" else "/$dataEntityId"}") {
                header("Content-Disposition", """attachment; filename="$name"""")
                body = ByteArrayContent(data, contentType = type)
            }
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(BlobBo.serializer(), text)
    }

    @PublicApi
    suspend fun blobRead(blobId: EntityId<BlobBo>): ByteArray {
        try {
            return client.get("$baseUrl/api/$namespace/blob/content/$blobId")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override suspend fun blobMetaList(dataEntityId: EntityId<T>): List<BlobBo> {

        val text = try {
            client.get<String>("$baseUrl/api/$namespace/blob/list/$dataEntityId")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(BlobBo.serializer()), text)
    }

    @PublicApi
    override suspend fun blobMetaRead(blobId: EntityId<BlobBo>): BlobBo {

        val text = try {
            client.get<String>("$baseUrl/api/$namespace/blob/meta/$blobId")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(BlobBo.serializer(), text)
    }

    @PublicApi
    override suspend fun blobMetaUpdate(dto: BlobBo): BlobBo {
        require(! dto.id.isEmpty()) { "ID of the $dto is 0 " }

        val text = try {
            client.patch<String>("$baseUrl/api/$namespace/blob/meta/${dto.id}") {
                header("Content-Type", "application/json")
                body = Json.encodeToString(BlobBo.serializer(), dto)
            }
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(BlobBo.serializer(), text)
    }

    @PublicApi
    override suspend fun blobDelete(blobId: EntityId<BlobBo>) {
        try {
            client.delete<Unit>("$baseUrl/api/$namespace/blob/$blobId")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

}