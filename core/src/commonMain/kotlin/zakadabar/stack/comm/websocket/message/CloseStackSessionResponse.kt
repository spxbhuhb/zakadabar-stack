/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.ResponseCode
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class CloseStackSessionResponse(
    override val responseCode: ResponseCode
) : Response {

    override var messageId = 0L

    override val messageType = MessageType.CLOSE_STACK_SESSION_RESPONSE

    override fun toString(): String = "${this::class.simpleName}(responseCode=$responseCode)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(ResponseCode.OK)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            CloseStackSessionResponse(
                readResponseCode()
            )
        }
    }
}