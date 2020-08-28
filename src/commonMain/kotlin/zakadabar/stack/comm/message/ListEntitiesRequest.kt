/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.MessageType
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray

class ListEntitiesRequest(
    val parent: Long
) : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.LIST_ENTITIES_REQUEST

    override fun toString(): String = "${this::class.simpleName}(parent=$parent)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(parent)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            ListEntitiesRequest(
                parent = readLong()
            )
        }
    }
}