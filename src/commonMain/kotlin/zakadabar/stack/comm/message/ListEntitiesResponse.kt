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