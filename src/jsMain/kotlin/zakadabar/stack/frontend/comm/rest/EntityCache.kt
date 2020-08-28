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
import zakadabar.stack.frontend.builtin.desktop.messages.EntityChildrenLoaded
import zakadabar.stack.frontend.comm.util.pushBlob
import zakadabar.stack.frontend.errors.FetchError
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.frontend.util.log
import zakadabar.stack.util.PublicApi

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
        return result
    }

    override suspend fun update(dto: EntityDto): EntityDto {
        val result = super.update(dto)
        cache[result.id] = result
        return result
    }


    @PublicApi
    fun launchCreateAndPush(parentId: Long?, file: File) {
        launch {
            val dto = create(EntityDto.new(parentId, file.name, file.type))

            pushBlob(dto.id, file)

            FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }
        }
    }

    @PublicApi
    fun launchCreateAndPush(parentId: Long?, name: String, type: String, byteArray: ByteArray) {
        launch {
            val dto = create(EntityDto.new(parentId, name, type))

            pushBlob(dto.id, Blob(byteArray.toTypedArray()))

            FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }
        }
    }

    @PublicApi
    fun launchGetChildren(parentId: Long?) {
        launch {
            if (children.containsKey(parentId)) {

                FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }

                return@launch
            }

            try {

                getChildrenOf(parentId)

                FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }

            } catch (ex: Throwable) {
                log(ex)
                val status = if (ex is FetchError) ex.response.status.toString() else "unknown"

                FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId, status) }
            }
        }
    }
}