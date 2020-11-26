/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class CreateBlobRequest(
    val name: String,
    val type: String,
    val size: Long
) : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.CREATE_BLOB_REQUEST

    override fun toString(): String = "${this::class.simpleName}(name=$name, type=$type, size=$size)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(name)
        write(type)
        write(size)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            CreateBlobRequest(
                name = readString(),
                type = readString(),
                size = readLong()
            )
        }
    }
}
