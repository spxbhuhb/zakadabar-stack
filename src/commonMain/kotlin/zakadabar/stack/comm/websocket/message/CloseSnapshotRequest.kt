/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class CloseSnapshotRequest(
    val snapshotId: Long
) : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.CLOSE_SNAPSHOT_REQUEST

    override fun toString(): String = "${this::class.simpleName}(snapshotId=$snapshotId)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(snapshotId)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            CloseSnapshotRequest(
                snapshotId = readLong()
            )
        }
    }

}
