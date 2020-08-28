/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.MessageType
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray

class CloseStackSessionRequest : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.CLOSE_STACK_SESSION_REQUEST

    override fun toString(): String = "${this::class.simpleName}()"

    override fun write(commArray: StackOutputCommArray) = Unit

    companion object {
        @Suppress("UNUSED_PARAMETER") // this is an interface method actually
        fun read(commArray: StackInputCommArray) = CloseStackSessionRequest()
    }
}
