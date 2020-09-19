/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

interface StackMessage {
    var messageId: Long
    val messageType: MessageType
    fun write(commArray: StackOutputCommArray)
}