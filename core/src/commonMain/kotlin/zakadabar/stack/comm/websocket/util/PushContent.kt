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
import zakadabar.stack.comm.websocket.message.CreateBlobRequest
import zakadabar.stack.comm.websocket.message.CreateBlobResponse
import zakadabar.stack.comm.websocket.message.WriteBlobRequest
import zakadabar.stack.comm.websocket.session.SessionError
import zakadabar.stack.comm.websocket.session.StackClientSession
import zakadabar.stack.data.BlobDto
import kotlin.math.min

/**
 * Uploads a complete entity snapshot to the server, handles the whole upload process:
 *
 * * Creates a new snapshot for the given entity.
 * * Pushes all the content into the snapshot. Takes maximum chunk size into account.
 * * Closes the snapshot.
 *
 * @property  session     the session to use for sending and receiving messages
 * @property  dto         DTO of the blob, id is ignored.
 * @property  readData    Function to read a part of the data.
 *                        First parameter is the position to read from, second is the number of bytes to read.
 * @property  onProgress  Callback to report the progress of push.
 *                        Called whenever a response is received from the server.
 */
class PushContent(
    private val session: StackClientSession,
    private var dto: BlobDto,
    private val readData: suspend (position: Long, size: Int) -> ByteArray,
    private val onProgress: (dto: BlobDto, state: PushState, position: Long) -> Unit = { _, _, _ -> }
) {
    companion object {
        const val CHUNK_SIZE = 900000L
        const val MAX_ACTIVE_REQUESTS = 3
    }

    class PushChunk(
        val position: Long,
        val size: Long
    )

    enum class PushState {
        Starting,
        Created,
        Progress,
        Finished,
        Error
    }

    private val chunkSize = min(dto.size, CHUNK_SIZE)
    private val fullChunkCount = dto.size / chunkSize
    private val lastChunkSize = dto.size % chunkSize
    private val totalChunkCount = if (lastChunkSize > 0) fullChunkCount + 1 else fullChunkCount

    /**
     * Run the content push process.
     *
     * @return  DTO of the blob created
     *
     * @throws  SessionError  The server returns with a non-OK response, network error.
     */
    suspend fun run(): BlobDto {
        coroutineScope {

            try {
                val response = session.send { CreateBlobRequest(dto.name, dto.type, dto.size) } as CreateBlobResponse
                dto = dto.copy(id = response.blobId)
                onProgress(dto, PushState.Created, 0L)

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
                    onProgress(dto, PushState.Progress, totalChunkCount * 100 / counter)
                    if (counter == totalChunkCount) break
                }

                onProgress(dto, PushState.Finished, totalChunkCount * 100 / counter)

            } catch (ex: Exception) {
                onProgress(dto, PushState.Error, 0L)
                throw ex
            }

        }

        return dto
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

                session.send { WriteBlobRequest(dto.id, chunk.position, data) }

                completed.send(chunk)
            }
        }
}

