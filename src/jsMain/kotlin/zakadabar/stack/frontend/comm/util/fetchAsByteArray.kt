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

package zakadabar.stack.frontend.comm.util

import zakadabar.stack.comm.util.FetchContent
import zakadabar.stack.frontend.FrontendContext

suspend fun fetchAsByteArray(entityId: Long, revision: Long): ByteArray {

    var array = ByteArray(0)

    @Suppress("RedundantSuspendModifier")
    suspend fun allocate(size: Long) {
        if (size > Int.MAX_VALUE) throw NotImplementedError("fetching data larger than 2GB is not implemented")
        array = ByteArray(size.toInt())
    }

    @Suppress("RedundantSuspendModifier")
    suspend fun writeData(position: Long, data: ByteArray) {
        data.copyInto(array, position.toInt(), 0, data.size)
    }

    FetchContent(FrontendContext.stackSession, entityId, revision, ::allocate, ::writeData).run()

    return array
}