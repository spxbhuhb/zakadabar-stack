/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class AddEntityRequest(
    val parentId: Long?,
    val name: String,
    val type: String
) : Request {

    override var messageId = 0L

    override val messageType = MessageType.ADD_ENTITY_REQUEST

    override fun toString(): String = "${this::class.simpleName}(parentId=$parentId, name=$name, type=$type)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(parentId)
        write(name)
        write(type)
    }

    companion object {

        fun read(commArray: StackInputCommArray) = with(commArray) {
            AddEntityRequest(
                parentId = readOptLong(),
                name = readString(),
                type = readString()
            )
        }

    }
}