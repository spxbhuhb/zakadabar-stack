/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class ReadBlobRequest(
    val blobId: Long,
    val position: Long,
    val size: Int
) : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.READ_BLOB_REQUEST

    override fun toString(): String =
        "${this::class.simpleName}(blobId=$blobId, position=$position, size=$size)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(blobId)
        write(position)
        write(size)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            ReadBlobRequest(
                blobId = readLong(),
                position = readLong(),
                size = readInt()
            )
        }
    }
}