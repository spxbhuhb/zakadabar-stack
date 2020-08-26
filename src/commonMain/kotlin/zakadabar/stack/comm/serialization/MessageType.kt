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

import zakadabar.stack.comm.message.*


enum class MessageType(val read: (StackInputCommArray) -> StackMessage) {
    FAULT_RESPONSE(FaultResponse.Companion::read),

    OPEN_STACK_SESSION_REQUEST(OpenStackSessionRequest.Companion::read),
    OPEN_STACK_SESSION_RESPONSE(OpenStackSessionResponse.Companion::read),

    CLOSE_STACK_SESSION_REQUEST(CloseStackSessionRequest.Companion::read),
    CLOSE_STACK_SESSION_RESPONSE(CloseStackSessionResponse.Companion::read),

    ADD_ENTITY_REQUEST(AddEntityRequest.Companion::read),
    ADD_ENTITY_RESPONSE(AddEntityResponse.Companion::read),

    FETCH_CONTENT_REQUEST(FetchContentRequest.Companion::read),
    FETCH_CONTENT_RESPONSE(FetchContentResponse.Companion::read),

    PUSH_CONTENT_REQUEST(PushContentRequest.Companion::read),
    PUSH_CONTENT_RESPONSE(PushContentResponse.Companion::read),

    LIST_ENTITIES_REQUEST(ListEntitiesRequest.Companion::read),
    LIST_ENTITIES_RESPONSE(ListEntitiesResponse.Companion::read),

    OPEN_SNAPSHOT_REQUEST(OpenSnapshotRequest.Companion::read),
    OPEN_SNAPSHOT_RESPONSE(OpenSnapshotResponse.Companion::read),

    CLOSE_SNAPSHOT_REQUEST(CloseSnapshotRequest.Companion::read),
    CLOSE_SNAPSHOT_RESPONSE(CloseSnapshotResponse.Companion::read),

}