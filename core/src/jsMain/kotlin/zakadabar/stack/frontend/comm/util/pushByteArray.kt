/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.util

import zakadabar.stack.comm.websocket.session.SessionError
import zakadabar.stack.comm.websocket.util.PushContent
import zakadabar.stack.data.BlobDto
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.util.PublicApi

/**
 * Helper method to push a byte array as entity content. Calls [PushContent].
 *
 * @param  name        Name of the blob (typically the name of the source file).
 * @param  type        Type of the blob (typically the mime type if the source file).
 * @param  data        The byte array to push.
 * @param  onProgress  Callback to report the progress of push.
 *                     Called whenever a response is received from the server.
 *
 * @return  id of the blob created
 *
 * @throws  SessionError  The server returns with a non-OK response, network error.
 */
@PublicApi
suspend fun pushByteArray(
    name: String,
    type: String,
    data: ByteArray,
    onProgress: (dto: BlobDto, state: PushContent.PushState, position: Long) -> Unit = { _, _, _ -> }
): BlobDto {

    @Suppress("RedundantSuspendModifier") // PushContent wants suspend
    suspend fun readData(position: Long, size: Int): ByteArray {

        val byteData = ByteArray(size)

        // FIXME optimize this
        for (i in 0 until size) {
            byteData[i] = data[(position + i).toInt()]
        }

        return byteData
    }

    val dto = BlobDto(0L, name, type, data.size.toLong())
    return PushContent(FrontendContext.stackSession, dto, ::readData, onProgress).run()
}
