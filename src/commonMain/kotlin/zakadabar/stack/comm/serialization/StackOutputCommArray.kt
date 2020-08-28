/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.serialization

import zakadabar.stack.comm.message.StackMessage
import zakadabar.stack.data.entity.EntityStatus

class StackOutputCommArray(initialSize: Int = 100) : OutputCommArray(initialSize) {

    fun write(responseCode: ResponseCode) {
        write(responseCode.ordinal)
    }

    fun write(value: EntityStatus) {
        write(value.ordinal)
    }

    fun write(value: MessageType) {
        write(value.ordinal)
    }

    fun write(message: StackMessage): StackOutputCommArray {
        write(message.messageId)
        write(message.messageType)
        message.write(this)
        return this
    }

}