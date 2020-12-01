/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.rest

import kotlinx.serialization.KSerializer
import org.w3c.files.Blob
import org.w3c.files.File
import zakadabar.stack.data.entity.ChildrenQuery
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.builtin.desktop.messages.EntityAdded
import zakadabar.stack.frontend.comm.rest.EntityCache.childrenOf
import zakadabar.stack.frontend.comm.rest.EntityCache.query
import zakadabar.stack.frontend.comm.rest.EntityCache.read
import zakadabar.stack.frontend.comm.util.pushBlob
import zakadabar.stack.frontend.errors.FetchError
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.frontend.util.log
import zakadabar.stack.util.PublicApi
import kotlin.collections.set

/**
 * A cache for entities.
 *
 * TODO Channel based synchronisation for EntityCache #2
 *
 * Methods [read], [childrenOf] and [query] should be send requests to a channel.
 * A receiver on the other side of the channel should coalesce similar requests, so
 * if two or more elements requests the same entity at the same time (which happens
 * a lot) only one request would be sent to the server.
 *
 */
@PublicApi
object EntityCache : RecordComm<EntityRecordDto>(EntityRecordDto.recordType, EntityRecordDto.serializer()) {

    private val cache = mutableMapOf<Long, EntityRecordDto>()

    val children = mutableMapOf<Long?, List<EntityRecordDto>>()

    @PublicApi
    fun clear() {
        cache.clear()
        children.clear()
    }

    override suspend fun read(id: Long): EntityRecordDto {
        val cached = cache[id]
        if (cached != null) return cached

        val result = super.read(id)
        cache[result.id] = result
        return result
    }

    suspend fun childrenOf(parentId: Long?): List<EntityRecordDto> {
        val cached = children[parentId]
        if (cached != null) return cached

        val result = ChildrenQuery(parentId).execute()

        result.forEach { cache[it.id] = it }

        children[parentId] = result

        return result
    }

    override suspend fun <RQ : Any> query(request: RQ, requestSerializer: KSerializer<RQ>): List<EntityRecordDto> {
        val result = super.query(request, requestSerializer)
        result.forEach { cache[it.id] = it }
        return result
    }

    override suspend fun create(dto: EntityRecordDto): EntityRecordDto {
        val result = super.create(dto)
        cache[result.id] = result
        FrontendContext.dispatcher.postSync { EntityAdded(dto) }
        return result
    }

    override suspend fun update(dto: EntityRecordDto): EntityRecordDto {
        val result = super.update(dto)
        cache[result.id] = result
        FrontendContext.dispatcher.postSync { EntityAdded(result) }
        return result
    }


    @PublicApi
    fun launchCreateAndPush(file: File) {
        launch {
            pushBlob(file.name, file.type, file)
        }
    }

    @PublicApi
    fun launchCreateAndPush(name: String, type: String, byteArray: ByteArray) {
        launch {
            pushBlob(name, type, Blob(byteArray.toTypedArray()))
        }
    }

    @PublicApi
    fun launchGetChildren(parentId: Long?, onFinish: (children: List<EntityRecordDto>, error: String?) -> Unit) {
        launch {
            try {
                onFinish(children[parentId] ?: childrenOf(parentId), null)
            } catch (ex: Throwable) {
                log(ex)
                val status = if (ex is FetchError) ex.response.status.toString() else "unknown"
                onFinish(emptyList(), status)
            }
        }
    }
}