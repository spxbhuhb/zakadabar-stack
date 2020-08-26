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

import zakadabar.stack.comm.session.SessionError
import zakadabar.stack.comm.util.PushContent
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.util.PublicApi

/**
 * Helper method to push a byte array as entity content. Calls [PushContent].
 *
 * @param  entityId    Id of the entity to push data for.
 * @param  data        The byte array to push.
 * @param  onProgress  Callback to report the progress of push.
 *                     Called whenever a response is received from the server.
 *
 * @return  id of the revision created
 *
 * @throws  SessionError  The server returns with a non-OK response, network error.
 */
@PublicApi
suspend fun pushByteArray(entityId: Long, data: ByteArray, onProgress: (position: Long) -> Unit = { }): Long {

    @Suppress("RedundantSuspendModifier") // PushContent wants suspend
    suspend fun readData(position: Long, size: Int): ByteArray {

        val byteData = ByteArray(size)

        // FIXME optimize this
        for (i in 0 until size) {
            byteData[i] = data[(position + i).toInt()]
        }

        return byteData
    }

    return PushContent(FrontendContext.stackSession, entityId, data.size.toLong(), ::readData, onProgress).run()
}
