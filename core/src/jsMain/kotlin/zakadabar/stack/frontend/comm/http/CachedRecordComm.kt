/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.http

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.util.PublicApi

/**
 * A cache for objects accessed with a [RecordComm]. This is basically a
 * wrapper around [RecordComm], that caches objects on the fly.
 *
 * Be careful with this one, you should probably use a [RecordComm].
 *
 * Caching objects in general is not a good idea as it is very easy
 * to use up a lot of memory. However, sometimes it is very useful.
 *
 * For example feeding selects which display the same data all over the
 * application, the list of buildings for example. Or in comments which
 * displays the user name and avatar.
 *
 * Using a cache has to be considered case-by-case basis, again, be careful.
 */
@PublicApi
open class CachedRecordComm<T : RecordDto<T>>(
    path: String,
    serializer: KSerializer<T>,
) : RecordComm<T>(path, serializer) {

    private val cache = mutableMapOf<Long, T>()

    @PublicApi
    fun clear() {
        cache.clear()
    }

    override suspend fun read(id: Long): T {
        val cached = cache[id]
        if (cached != null) return cached

        val result = super.read(id)
        cache[result.id] = result
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