/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.ResponseCode
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class PushContentResponse(
    override val responseCode: ResponseCode,
    val snapshotId: Long,
    val position: Long,
    val size: Int
) : Response {
    override var messageId = 0L

    override val messageType = MessageType.PUSH_CONTENT_RESPONSE

    override fun toString(): String =
        "${this::class.simpleName}(responseCode=$responseCode, snapshotId=$snapshotId, position=$position, size=$size)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(responseCode)
        write(snapshotId)
        write(responseCode)
        write(size)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            PushContentResponse(
                responseCode = readResponseCode(),
                snapshotId = readLong(),
                position = readLong(),
                size = readInt()
            )
        }
    }
}


