/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class OpenSnapshotRequest(
    val entityId: Long,
    val revision: Long? = null
) : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.OPEN_SNAPSHOT_REQUEST

    override fun toString(): String = "${this::class.simpleName}(entityId=$entityId, revision=$revision)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(entityId)
        write(revision)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            OpenSnapshotRequest(
                entityId = readLong(),
                revision = readOptLong()
            )
        }
    }
}
