/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.ResponseCode
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class ReadBlobResponse(
    override val responseCode: ResponseCode,
    val blobId: Long,
    val position: Long,
    val data: ByteArray
) : Response {

    override var messageId = 0L

    override val messageType = MessageType.READ_BLOB_RESPONSE

    override fun toString(): String =
        "${this::class.simpleName}(blobId=$blobId, position=$position, data.size=${data.size})"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(responseCode)
        write(blobId)
        write(position)
        write(data)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            ReadBlobResponse(
                readResponseCode(),
                blobId = readLong(),
                position = readLong(),
                data = readByteArray()
            )
        }
    }
}
