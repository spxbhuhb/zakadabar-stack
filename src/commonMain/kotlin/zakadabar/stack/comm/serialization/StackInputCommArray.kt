/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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