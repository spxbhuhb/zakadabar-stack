/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.frontend.comm.rest

import kotlinx.serialization.KSerializer
import zakadabar.stack.extend.DtoWithRecordContract
import zakadabar.stack.util.PublicApi

/**
 * A cache for objects accessed with a [RecordRestComm]. This is basically a
 * wrapper around [RecordRestComm], that caches objects on the fly.
 *
 * Be careful with this one, you should probably use a [RecordRestComm].
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
open class CachedRecordRestComm<T : DtoWithRecordContract<T>>(
    path: String,
    serializer: KSerializer<T>,
) : RecordRestComm<T>(path, serializer) {

    private val cache = mutableMapOf<Long, T>()

    @PublicApi
    fun clear() {
        cache.clear()
    }

    override suspend fun get(id: Long): T {
        val cached = cache[id]
        if (cached != null) return cached

        val result = super.get(id)
        cache[result.id] = result
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