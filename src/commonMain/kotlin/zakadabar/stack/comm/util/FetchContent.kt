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

package zakadabar.stack.comm.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import zakadabar.stack.comm.message.*
import zakadabar.stack.comm.session.SessionError
import zakadabar.stack.comm.session.StackClientSession
import kotlin.math.min

/**
 * Downloads a complete entity snapshot from the server, handles the whole download process:
 *
 * * Opens an existing snapshot of the entity.
 * * Fetches all the content into the snapshot. Takes maximum chunk size into account.
 * * Closes the snapshot.
 *
 * @property  session          the session to use for sending and receiving messages
 * @property  entityId         Id of the entity to push data for.
 * @property  revision         Revision to open the snapshot for.
 * @property  prepareForWrite  Perform preparations before the first [writeData] call.
 *                             Parameter is the size of data that will be fetched from the server.
 * @property  writeData        Function to write a part of the data.
 *                             First parameter is the position to write to, second is the number of bytes to write.
 * @property  onProgress       Callback to report the progress of download.
 *                             Called whenever a response is received from the server.
 */
class FetchContent(
    private val session: StackClientSession,
    private val entityId: Long,
    private val revision: Long,
    private val prepareForWrite: suspend (size: Long) -> Unit = { },
    private val writeData: suspend (position: Long, data: ByteArray) -> Unit,
    private val onProgress: (position: Long) -> Unit = { }
) {
    companion object {
        const val CHUNK_SIZE = 900000L
        const val MAX_ACTIVE_REQUESTS = 3
    }

    class FetchChunk(
        val position: Long,
        val size: Int
    )

    private var chunkSize = 0L
    private var fullChunkCount = 0L
    private var lastChunkSize = 0L
    private var totalChunkCount = 0L

    private var snapshotId = 0L

    /**
     * Run the content push process.
     *
     * @throws  SessionError  The server returns with a non-OK response, network error.
     */
    suspend fun run() {
        coroutineScope {

            openSnapshot()

            val chunkProducer = chunksChannel()

            val completed = Channel<FetchChunk>()

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
    }

    private suspend fun openSnapshot() {
        val response = session.send { OpenSnapshotRequest(entityId, revision) } as OpenSnapshotResponse

        snapshotId = response.snapshotId
        val dataSize = response.size

        if (dataSize != 0L) {
            chunkSize = min(dataSize, CHUNK_SIZE)
            fullChunkCount = dataSize / chunkSize
            lastChunkSize = dataSize % chunkSize
            totalChunkCount = if (lastChunkSize > 0) fullChunkCount + 1 else fullChunkCount
        }

        prepareForWrite(dataSize)
    }

    private suspend fun closeSnapshot() {
        session.send { CloseSnapshotRequest(snapshotId) }
    }

    private fun CoroutineScope.chunksChannel(): ReceiveChannel<FetchChunk> = produce {

        var sent = 0

        while (sent < fullChunkCount) {
            send(FetchChunk(sent * chunkSize, chunkSize.toInt()))
            sent ++
        }

        if (lastChunkSize > 0) {
            send(FetchChunk(sent * chunkSize, lastChunkSize.toInt()))
        }
    }

    private fun CoroutineScope.uploadProcessor(channel: ReceiveChannel<FetchChunk>, completed: Channel<FetchChunk>) =
        launch {

            for (chunk in channel) {

                val response =
                    session.send { FetchContentRequest(snapshotId, chunk.position, chunk.size) } as FetchContentResponse

                writeData(chunk.position, response.data)

                completed.send(chunk)
            }
        }
}

