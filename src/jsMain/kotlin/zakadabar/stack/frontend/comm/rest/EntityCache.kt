/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.rest

import org.w3c.files.Blob
import org.w3c.files.File
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.extend.EntityRestCommContract
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.builtin.desktop.messages.EntityAdded
import zakadabar.stack.frontend.comm.rest.EntityCache.get
import zakadabar.stack.frontend.comm.rest.EntityCache.getChildrenOf
import zakadabar.stack.frontend.comm.rest.EntityCache.search
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
 * Methods [get], [getChildrenOf] and [search] should be send requests to a channel.
 * A receiver on the other side of the channel should coalesce similar requests, so
 * if two or more elements requests the same entity at the same time (which happens
 * a lot) only one request would be sent to the server.
 *
 */
@PublicApi
object EntityCache : RecordRestComm<EntityDto>("${Stack.shid}/entities", EntityDto.serializer()),
    EntityRestCommContract<EntityDto> {

    private val cache = mutableMapOf<Long, EntityDto>()

    val children = mutableMapOf<Long?, List<EntityDto>>()

    @PublicApi
    fun clear() {
        cache.clear()
        children.clear()
    }

    override suspend fun get(id: Long): EntityDto {
        val cached = cache[id]
        if (cached != null) return cached

        val result = super.get(id)
        cache[result.id] = result
        return result
    }

    override suspend fun getChildrenOf(parentId: Long?): List<EntityDto> {
        val cached = children[parentId]
        if (cached != null) return cached

        val result = getChildrenOf(parentId, path, serializer)

        result.forEach { cache[it.id] = it }

        return result
    }

    override suspend fun search(parameters: String): List<EntityDto> {
        val result = super.search(parameters)
        result.forEach { cache[it.id] = it }
        return result
    }

    override suspend fun create(dto: EntityDto): EntityDto {
        val result = super.create(dto)
        cache[result.id] = result
        FrontendContext.dispatcher.postSync { EntityAdded(dto) }
        return result
    }

    override suspend fun update(dto: EntityDto): EntityDto {
        val result = super.update(dto)
        cache[result.id] = result
        FrontendContext.dispatcher.postSync { EntityAdded(result) }
        return result
    }


    @PublicApi
    fun launchCreateAndPush(parentId: Long?, file: File) {
        launch {
            val dto = create(EntityDto.new(parentId, file.type, file.name))
            pushBlob(dto.id, file)
        }
    }

    @PublicApi
    fun launchCreateAndPush(parentId: Long?, name: String, type: String, byteArray: ByteArray) {
        launch {
            val dto = create(EntityDto.new(parentId, name, type))
            pushBlob(dto.id, Blob(byteArray.toTypedArray()))
        }
    }

    @PublicApi
    fun launchGetChildren(parentId: Long?, onFinish: (children: List<EntityDto>, error: String?) -> Unit) {
        launch {
            try {
                onFinish(children[parentId] ?: getChildrenOf(parentId), null)
            } catch (ex: Throwable) {
                log(ex)
                val status = if (ex is FetchError) ex.response.status.toString() else "unknown"
                onFinish(emptyList(), status)
            }
        }
    }
}