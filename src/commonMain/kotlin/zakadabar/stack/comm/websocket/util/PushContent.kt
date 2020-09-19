/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import zakadabar.stack.comm.websocket.message.*
import zakadabar.stack.comm.websocket.session.SessionError
import zakadabar.stack.comm.websocket.session.StackClientSession
import kotlin.math.min

/**
 * Uploads a complete entity snapshot to the server, handles the whole upload process:
 *
 * * Creates a new snapshot for the given entity.
 * * Pushes all the content into the snapshot. Takes maximum chunk size into account.
 * * Closes the snapshot.
 *
 * @property  session     the session to use for sending and receiving messages
 * @property  entityId    Id of the entity to push data for.
 * @property  dataSize    The size of the complete data to push.
 * @property  readData    Function to read a part of the data.
 *                        First parameter is the position to read from, second is the number of bytes to read.
 * @property  onProgress  Callback to report the progress of push.
 *                        Called whenever a response is received from the server.
 */
class PushContent(
    private val session: StackClientSession,
    private val entityId: Long,
    private val dataSize: Long,
    private val readData: suspend (position: Long, size: Int) -> ByteArray,
    private val onProgress: (position: Long) -> Unit = { }
) {
    companion object {
        const val CHUNK_SIZE = 900000L
        const val MAX_ACTIVE_REQUESTS = 3
    }

    class PushChunk(
        val position: Long,
        val size: Long
    )

    private val chunkSize = min(dataSize, CHUNK_SIZE)
    private val fullChunkCount = dataSize / chunkSize
    private val lastChunkSize = dataSize % chunkSize
    private val totalChunkCount = if (lastChunkSize > 0) fullChunkCount + 1 else fullChunkCount

    private var snapshotId = 0L
    private var revision = 0L

    /**
     * Run the content push process.
     *
     * @return  the revision created
     *
     * @throws  SessionError  The server returns with a non-OK response, network error.
     */
    suspend fun run(): Long {
        coroutineScope {

            openSnapshot()

            val chunkProducer = chunksChannel()

            val completed = Channel<PushChunk>()

            launch {
                repeat(MAX_ACTIVE_REQUESTS) {
                    uploadProcessor(chunkProducer, completed)
                }
            }

            var counter = 0L

            for (chunk in completed) {
                counter ++
                onProgress(totalChunkCount * 100 / counter)
                if (counter == totalChunkCount) break
            }

            closeSnapshot()
        }

        return revision
    }

    private suspend fun openSnapshot() {
        val response = session.send { OpenSnapshotRequest(entityId) } as OpenSnapshotResponse
        snapshotId = response.snapshotId
    }

    private suspend fun closeSnapshot() {
        val response = session.send { CloseSnapshotRequest(snapshotId) } as CloseSnapshotResponse
        revision = response.revision
    }

    private fun CoroutineScope.chunksChannel(): ReceiveChannel<PushChunk> = produce {

        var sent = 0

        while (sent < fullChunkCount) {
            send(PushChunk(sent * chunkSize, chunkSize))
            sent ++
        }

        if (lastChunkSize > 0) {
            send(PushChunk(sent * chunkSize, lastChunkSize))
        }
    }

    private fun CoroutineScope.uploadProcessor(channel: ReceiveChannel<PushChunk>, completed: Channel<PushChunk>) =
        launch {

            for (chunk in channel) {

                val data = readData(chunk.position, chunk.size.toInt())

                session.send { PushContentRequest(snapshotId, chunk.position, data) }

                completed.send(chunk)
            }
        }
}

