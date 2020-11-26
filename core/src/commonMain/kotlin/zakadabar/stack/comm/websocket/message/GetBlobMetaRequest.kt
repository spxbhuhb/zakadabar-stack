/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class GetBlobMetaRequest(
    val blobId: Long
) : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.GET_BLOB_META_REQUEST

    override fun toString(): String = "${this::class.simpleName}(blobId=$blobId)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(blobId)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            GetBlobMetaRequest(
                blobId = readLong()
            )
        }
    }
}
