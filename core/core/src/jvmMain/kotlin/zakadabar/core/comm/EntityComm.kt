/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import io.ktor.client.request.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommBase.Companion.client
import zakadabar.core.comm.CommBase.Companion.onError
import zakadabar.core.comm.CommConfig.Companion.localEntityBl
import zakadabar.core.comm.CommConfig.Companion.merge
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.util.PublicApi

/**
 * REST communication functions for entities.
 *
 * @property  companion    The companion object for this entity BO.
 *
 * @property  serializer   The serializer to serialize/deserialize objects
 *                         sent/received.
 */
@PublicApi
open class EntityComm<T : EntityBo<T>>(
    val companion: EntityBoCompanion<T>,
    val serializer: KSerializer<T>
) : EntityCommInterface<T> {

    @PublicApi
    override suspend fun create(bo: T, executor: Executor?, config: CommConfig?): T {
        require(bo.id.isEmpty()) { "id is empty in $bo" }

        localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.createWrapper(executor, bo)
        }

        val url = merge("/entity", companion.boNamespace, config, companion.commConfig)

        val text = try {
            client.post<String>(url) {
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
    override suspend fun read(id: EntityId<T>, executor: Executor?, config: CommConfig?): T {

        localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.readWrapper(executor, id)
        }

        val url = merge("/entity/$id", companion.boNamespace, config, companion.commConfig)

        val text = try {
            client.get<String>(url)
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun update(bo: T, executor: Executor?, config: CommConfig?): T {
        require(! bo.id.isEmpty()) { "ID of the $bo is 0 " }

        localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.updateWrapper(executor, bo)
        }

        val url = merge("/entity/${bo.id}", companion.boNamespace, config, companion.commConfig)

        val text = try {
            client.patch<String>(url) {
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
    override suspend fun all(executor: Executor?, config: CommConfig?): List<T> {

        localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.listWrapper(executor)
        }

        val url = merge("/entity", companion.boNamespace, config, companion.commConfig)

        val text = try {
            client.get<String>(url)
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: EntityId<T>, executor: Executor?, config: CommConfig?) {

        localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.deleteWrapper(executor, id)
        }

        val url = merge("/entity/$id", companion.boNamespace, config, companion.commConfig)

        try {
            client.delete<Unit>(url)
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

}