/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.util

import zakadabar.stack.comm.websocket.session.SessionError
import zakadabar.stack.comm.websocket.util.PushContent
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
