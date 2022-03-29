/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.comm

import io.ktor.client.request.*
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommBase.Companion.client
import zakadabar.core.comm.CommBase.Companion.onError
import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.CommConfig.Companion.commScope
import zakadabar.core.comm.CommConfig.Companion.merge
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.util.PublicApi
import zakadabar.lib.blobs.business.BlobBlBase
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.lib.blobs.data.BlobCreateState

/**
 * REST communication functions for entities.
 *
 * @property  companion    Companion object that belongs to the BO.
 *
 * @property  serializer   The serializer to serialize/deserialize objects
 *                         sent/received.
 */
@PublicApi
open class BlobComm<T : BlobBo<T, RT>, RT : EntityBo<RT>>(
    private val companion: BlobBoCompanion<T, RT>,
    private val serializer: KSerializer<T>
) : BlobCommInterface<T,RT> {

    @PublicApi
    override suspend fun create(bo: T, executor: Executor?, config: CommConfig?): T {
        require(bo.id.isEmpty()) { "id is empty in $bo" }

        CommConfig.localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.createWrapper(executor, bo)
        }

        val url = merge("/blob/meta", companion.boNamespace, config, companion.commConfig)

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

        CommConfig.localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.readWrapper(executor, id)
        }

        val url = merge("/blob/meta/$id", companion.boNamespace, config, companion.commConfig)

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

        CommConfig.localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.updateWrapper(executor, bo)
        }

        val url = merge("/blob/meta/${bo.id}", companion.boNamespace, config, companion.commConfig)

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

        val url = merge("/blob/meta", companion.boNamespace, config, companion.commConfig)

        CommConfig.localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.listWrapper(executor)
        }

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

        CommConfig.localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            return it.deleteWrapper(executor, id)
        }

        val url = merge("/blob/meta/$id", companion.boNamespace, config, companion.commConfig)

        try {
            client.delete<Unit>(url)
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override suspend fun upload(bo : T, data: Any, executor: Executor?, config: CommConfig?) : T {
        val channel = Channel<Boolean>()

        upload(bo, data, executor, config) { _, state, _ ->
            commScope.launch(Dispatchers.Default) {
                when (state) {
                    BlobCreateState.Error -> channel.send(false)
                    BlobCreateState.Done -> channel.send(true)
                    else -> Unit
                }
            }
        }

        if (!channel.receive()) throw RuntimeException("blob upload error")

        return bo
    }

    @PublicApi
    override suspend fun upload(bo : T, data: Any, executor: Executor?, config: CommConfig?, callback: (bo: T, state: BlobCreateState, uploaded: Long) -> Unit) : T {
        require(data is ByteArray)

        callback(bo, BlobCreateState.Starting, 0)

        CommConfig.localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            @Suppress("UNCHECKED_CAST")
            it as BlobBlBase<T,RT>
            it.writeContent(executor, bo.id, data.size.toLong(), data)
            callback(bo, BlobCreateState.Done, data.size.toLong())
            bo.size = data.size.toLong()
            return bo
        }

        commScope.launch(Dispatchers.Default) {

            val url = merge("/blob/content/${bo.id}", companion.boNamespace, config, companion.commConfig)

            try {
                client.post<String>(url) {
                    body = ByteArrayContent(data)
                }

                callback(bo, BlobCreateState.Done, data.size.toLong())

            } catch (ex: Exception) {
                callback(bo, BlobCreateState.Error, 0L)
                onError(ex)
                throw ex
            }
        }

        bo.size = data.size.toLong()

        return bo
    }

    @PublicApi
    override suspend fun download(id: EntityId<T>, executor: Executor?, config: CommConfig?): ByteArray {

        CommConfig.localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            @Suppress("UNCHECKED_CAST")
            it as BlobBlBase<T,RT>
            return it.readContent(executor, id) {  }.first
        }

        val url = merge("/blob/content/$id", companion.boNamespace, config, companion.commConfig)

        return try {
            client.get(url)
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override suspend fun byReference(reference: EntityId<RT>?, disposition : String?, executor: Executor?, config: CommConfig?): List<T> {
        val q = disposition?.let { "?disposition=$it" } ?: ""

        CommConfig.localEntityBl<T>(companion.boNamespace, config, companion.commConfig)?.let {
            requireNotNull(executor) { "for local calls the executor parameter is mandatory" }
            @Suppress("UNCHECKED_CAST")
            it as BlobBlBase<T,RT>
            return it.byReference(executor, reference, disposition)
        }

        val url = merge("/blob/list/${if (reference == null) "" else "/$reference"}$q", companion.boNamespace, config, companion.commConfig)

        val text = try {
            client.get<String>(url)
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

}