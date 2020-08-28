/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.MessageType
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray

class FetchContentRequest(
    val snapshotId: Long,
    val position: Long,
    val size: Int
) : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.FETCH_CONTENT_REQUEST

    override fun toString(): String =
        "${this::class.simpleName}(snapshotId=$snapshotId, position=$position, size=$size)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(snapshotId)
        write(position)
        write(size)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            FetchContentRequest(
                snapshotId = readLong(),
                position = readLong(),
                size = readInt()
            )
        }
    }
}