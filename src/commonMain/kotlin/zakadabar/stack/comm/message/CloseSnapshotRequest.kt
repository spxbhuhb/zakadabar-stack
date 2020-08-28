/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.MessageType
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray

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
