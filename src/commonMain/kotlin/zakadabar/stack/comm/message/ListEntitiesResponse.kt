/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.MessageType
import zakadabar.stack.comm.serialization.ResponseCode
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray
import zakadabar.stack.data.entity.EntityDto

class ListEntitiesResponse(
    override val responseCode: ResponseCode,
    val entities: List<EntityDto>
) : Response {

    override var messageId = 0L

    override val messageType = MessageType.LIST_ENTITIES_RESPONSE

    override fun toString(): String = "${this::class.simpleName}(responseCode=$responseCode, entities=$entities)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(responseCode)
        write(entities.size)
        entities.forEach {
            it.write(commArray)
        }
    }

    fun EntityDto.write(commArray: StackOutputCommArray) {
        with(commArray) {
            write(id)
            write(acl)
            write(status)
            write(parentId)
            write(type)
            write(name)
            write(size)
            write(revision)
            write(modifiedBy)
            write(modifiedAt)
        }
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {

            val responseCode = readResponseCode()
            val size = readInt()

            val entities = mutableListOf<EntityDto>()

            for (index in 0 until size) {
                entities += readEntity(commArray)
            }

            ListEntitiesResponse(
                responseCode = responseCode,
                entities = entities
            )
        }

        private fun readEntity(commArray: StackInputCommArray): EntityDto {
            with(commArray) {
                return EntityDto(
                    id = readLong(),
                    acl = readLong(),
                    status = readEntityStatus(),
                    parentId = readOptLong(),
                    type = readString(),
                    name = readString(),
                    size = readLong(),
                    revision = readLong(),
                    modifiedBy = readLong(),
                    modifiedAt = readLong()
                )
            }
        }

    }
}