/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import io.ktor.client.request.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.data.CommBase.Companion.baseUrl
import zakadabar.stack.data.CommBase.Companion.client
import zakadabar.stack.data.CommBase.Companion.onError
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

}