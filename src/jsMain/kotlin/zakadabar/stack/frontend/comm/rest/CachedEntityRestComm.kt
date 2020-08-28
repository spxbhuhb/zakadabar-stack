/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.rest

import kotlinx.serialization.KSerializer
import zakadabar.stack.extend.DtoWithEntityContract
import zakadabar.stack.util.PublicApi

/**
 * A cache for objects accessed with a [EntityRestComm]. This is basically a
 * wrapper around [EntityRestComm], that caches objects on the fly.
 *
 * Be careful with this one, you should probably use a [EntityRestComm].
 *
 * Caching objects in general is not a good idea as it is very easy
 * to use up a lot of memory. However, sometimes it is very useful.
 *
 * For example feeding selects which display the same data all over the
 * application, the list of buildings for example. Or in comments which
 * displays the user name and avatar.
 *
 * Using a cache has to be considered case-by-case basis, again, be careful.
 *
 * @property  childInfo  When true, the cache will store the structural information
 *                       about children, namely, it will store the list of children
 *                       for each parent.In this case [getChildren] returns with the
 *                       stored information instead of fetching the list of children
 *                       from the server.
 *
 */
@PublicApi
open class CachedEntityRestComm<T : DtoWithEntityContract<T>>(
    path: String,
    serializer: KSerializer<T>
) : EntityRestComm<T>(path, serializer) {

    private val cache = mutableMapOf<Long, T>()

    protected val children = mutableMapOf<Long?, List<T>>()

    @PublicApi
    fun clear() {
        cache.clear()
        children.clear()
    }

    override suspend fun get(id: Long): T {
        val cached = cache[id]
        if (cached != null) return cached

        val result = super.get(id)
        cache[result.id] = result
        return result
    }

    override suspend fun getChildrenOf(parentId: Long?): List<T> {
        val cached = children[parentId]
        if (cached != null) return cached

        val result = super.getChildrenOf(parentId)
        result.forEach { cache[it.id] = it }
        return result
    }

    override suspend fun search(parameters: String): List<T> {
        val result = super.search(parameters)
        result.forEach { cache[it.id] = it }
        return result
    }

    override suspend fun create(dto: T): T {
        val result = super.create(dto)
        cache[result.id] = result
        return result
    }

    override suspend fun update(dto: T): T {
        val result = super.update(dto)
        cache[result.id] = result
        return result
    }

}