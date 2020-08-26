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
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray

class OpenSnapshotRequest(
    val entityId: Long,
    val revision: Long? = null
) : StackMessage {

    override var messageId = 0L

    override val messageType = MessageType.OPEN_SNAPSHOT_REQUEST

    override fun toString(): String = "${this::class.simpleName}(entityId=$entityId, revision=$revision)"

    override fun write(commArray: StackOutputCommArray) = with(commArray) {
        write(entityId)
        write(revision)
    }

    companion object {
        fun read(commArray: StackInputCommArray) = with(commArray) {
            OpenSnapshotRequest(
                entityId = readLong(),
                revision = readOptLong()
            )
        }
    }
}
