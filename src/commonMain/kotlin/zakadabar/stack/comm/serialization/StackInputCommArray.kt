/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.serialization

import zakadabar.stack.comm.message.StackMessage
import zakadabar.stack.data.entity.EntityStatus

class StackInputCommArray(source: ByteArray) : InputCommArray(source) {

    fun readResponseCode(): ResponseCode {
        return responseCodes[readInt()]
    }

    fun readMessageType(): MessageType {
        return messageTypes[readInt()]
    }

    fun readMessage(): StackMessage {
        val commMessageId = readLong()
        val message = readMessageType().read(this)
        message.messageId = commMessageId
        return message
    }

    fun readEntityStatus(): EntityStatus {
        return entityStatuses[readInt()]
    }

    companion object {
        val entityStatuses = EntityStatus.values()
        val messageTypes = MessageType.values()
        val responseCodes = ResponseCode.values()
    }
}