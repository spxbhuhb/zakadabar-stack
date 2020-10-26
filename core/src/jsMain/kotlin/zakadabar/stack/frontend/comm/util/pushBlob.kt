/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.util

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.dom.events.Event
import org.w3c.files.Blob
import org.w3c.files.FileReader
import zakadabar.stack.comm.websocket.session.SessionError
import zakadabar.stack.comm.websocket.util.PushContent
import zakadabar.stack.frontend.FrontendContext

/**
 * Helper method to push a whole Javascript file as entity content. Calls [PushContent].
 *
 * @param  entityId    Id of the entity to push data for.
 * @param  blob        The blob to push content from.
 * @param  onProgress  Callback to report the progress of push.
 *                     Called whenever a response is received from the server.
 *
 * @return  id of the revision created
 *
 * @throws  SessionError  The server returns with a non-OK response, network error.
 */
suspend fun pushBlob(entityId: Long, blob: Blob, onProgress: (position: Long) -> Unit = { }): Long {

    // using this mutex to serialize file reads, without this the chunks will be empty
    // TODO investigate async Javascript file read in different browsers

    val readMutex = Mutex()

    suspend fun readData(position: Long, size: Int): ByteArray {
        readMutex.withLock {

            val deferred = CompletableDeferred<Unit>()

            val onLoadEnd = fun(_: Event) { deferred.complete(Unit) }

            val reader = FileReader()
            reader.onloadend = onLoadEnd
            reader.readAsArrayBuffer(
                blob.slice(
                    position.toInt(),
                    position.toInt() + size,
                    "application/octet-stream"
                )
            )

            deferred.await()

            if (reader.error != null) {
                throw RuntimeException(reader.error.toString())
            }

            if (reader.readyState.toInt() != 2) { // 2 = DONE
                throw RuntimeException("ERROR read finished with status ${reader.readyState}")
            }

            // FIXME this should be done by casting if possible
            val data = Int8Array(reader.result as ArrayBuffer)
            val byteData = ByteArray(data.length)

            for (i in 0 until data.length) {
                byteData[i] = data[i]
            }

            return byteData
        }
    }

    return PushContent(FrontendContext.stackSession, entityId, blob.size.toLong(), ::readData, onProgress).run()
}
