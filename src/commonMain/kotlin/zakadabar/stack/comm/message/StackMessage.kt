/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.MessageType
import zakadabar.stack.comm.serialization.StackOutputCommArray

interface StackMessage {
    var messageId: Long
    val messageType: MessageType
    fun write(commArray: StackOutputCommArray)
}