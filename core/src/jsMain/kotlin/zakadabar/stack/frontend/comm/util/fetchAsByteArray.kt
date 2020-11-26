/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.util

import zakadabar.stack.comm.websocket.util.FetchContent
import zakadabar.stack.frontend.FrontendContext

suspend fun fetchAsByteArray(blobId: Long): ByteArray {

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

    FetchContent(FrontendContext.stackSession, blobId, ::allocate, ::writeData).run()

    return array
}