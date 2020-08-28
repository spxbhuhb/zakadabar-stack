/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.MessageType
import zakadabar.stack.comm.serialization.ResponseCode
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray

class CloseSnapshotResponse(
    override val responseCode: ResponseCode,
    val snapshotId: Long,
    val revision: Long
) : Response {

    override var messageId = 0L

    override val messageType = MessageType.CLOSE_SNAPSHOT_RESPONSE

    override fun toString(): String = "${this::class.simpleName}(responseCode=$responseCode, snapshotId=$snapshotId)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(responseCode)
        write(snapshotId)
        write(revision)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            CloseSnapshotResponse(
                readResponseCode(),
                snapshotId = readLong(),
                revision = readLong()
            )
        }
    }
}
