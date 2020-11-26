/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.message

import zakadabar.stack.comm.websocket.serialization.MessageType
import zakadabar.stack.comm.websocket.serialization.ResponseCode
import zakadabar.stack.comm.websocket.serialization.StackInputCommArray
import zakadabar.stack.comm.websocket.serialization.StackOutputCommArray

class GetBlobMetaResponse(
    override val responseCode: ResponseCode,
    val blobId: Long,
    val name: String,
    val type: String,
    val size: Long
) : Response {

    override var messageId = 0L

    override val messageType = MessageType.GET_BLOB_META_RESPONSE

    override fun toString(): String =
        "${this::class.simpleName}(responseCode=$responseCode, blob=$blobId, name=$name, type=$type, size=$size)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(responseCode)
        write(blobId)
        write(name)
        write(type)
        write(size)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            GetBlobMetaResponse(
                readResponseCode(),
                blobId = readLong(),
                name = readString(),
                type = readString(),
                size = readLong()
            )
        }
    }
}