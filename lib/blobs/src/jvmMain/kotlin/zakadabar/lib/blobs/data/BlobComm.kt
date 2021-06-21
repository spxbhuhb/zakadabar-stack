/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import io.ktor.client.request.*
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.data.CommBase.Companion.baseUrl
import zakadabar.stack.data.CommBase.Companion.client
import zakadabar.stack.data.CommBase.Companion.onError
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
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
open class BlobComm<T : BlobBo<T,RT>, RT : EntityBo<RT>>(
    private val namespace: String,
    private val serializer: KSerializer<T>
) : BlobCommInterface<T,RT> {

    @PublicApi
    override suspend fun create(bo: T): T {
        require(bo.id.isEmpty()) { "id is empty in $bo" }

        val text = try {
            client.post<String>("$baseUrl/api/$namespace/blob/meta") {
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
            client.get<String>("$baseUrl/api/$namespace/blob/meta/$id")
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
            client.patch<String>("$baseUrl/api/$namespace/blob/meta/${bo.id}") {
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
            client.get<String>("$baseUrl/api/$namespace/blob/meta")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: EntityId<T>) {
        try {
            client.delete<Unit>("$baseUrl/api/$namespace/blob/meta/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override suspend fun upload(bo : T, data: Any, callback: (bo: T, state: BlobCreateState, uploaded: Long) -> Unit) {
        require(data is ByteArray)

        callback(bo, BlobCreateState.Starting, 0)

        GlobalScope.launch(Dispatchers.Default) {

            try {
                client.post<String>("$baseUrl/api/$namespace/blob/content/${bo.id}") {
                    body = ByteArrayContent(data)
                }

                callback(bo, BlobCreateState.Done, data.size.toLong())

            } catch (ex: Exception) {
                callback(bo, BlobCreateState.Error, 0L)
                onError(ex)
                throw ex
            }
        }
    }

    @PublicApi
    override suspend fun download(id: EntityId<T>): ByteArray {
        return try {
            client.get<ByteArray>("$baseUrl/api/$namespace/blob/content/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override suspend fun listByReference(reference: EntityId<RT>, disposition : String?): List<T> {
        val q = disposition?.let { "?disposition=$it" } ?: ""
        val text = try {
            client.get<String>("$baseUrl/api/$namespace/blob/list/$reference$q")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

}