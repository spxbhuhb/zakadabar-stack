/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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