/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.MessageType
import zakadabar.stack.comm.serialization.ResponseCode
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray

class AddEntityResponse(
    override val responseCode: ResponseCode,
    val entityId: Long
) : Response {

    override var messageId = 0L

    override val messageType = MessageType.ADD_ENTITY_RESPONSE

    override fun toString(): String = "${this::class.simpleName}(responseCode=$responseCode, entityId=$entityId)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(responseCode)
        write(entityId)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            AddEntityResponse(
                readResponseCode(),
                readLong()
            )
        }
    }
}