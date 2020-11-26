/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.serialization

import zakadabar.stack.comm.websocket.message.*


enum class MessageType(val read: (StackInputCommArray) -> StackMessage) {
    FAULT_RESPONSE(FaultResponse.Companion::read),

    OPEN_STACK_SESSION_REQUEST(OpenStackSessionRequest.Companion::read),
    OPEN_STACK_SESSION_RESPONSE(OpenStackSessionResponse.Companion::read),

    CLOSE_STACK_SESSION_REQUEST(CloseStackSessionRequest.Companion::read),
    CLOSE_STACK_SESSION_RESPONSE(CloseStackSessionResponse.Companion::read),

    ADD_ENTITY_REQUEST(AddEntityRequest.Companion::read),
    ADD_ENTITY_RESPONSE(AddEntityResponse.Companion::read),

    READ_BLOB_REQUEST(ReadBlobRequest.Companion::read),
    READ_BLOB_RESPONSE(ReadBlobResponse.Companion::read),

    WRITE_BLOB_REQUEST(WriteBlobRequest.Companion::read),
    WRITE_BLOB_RESPONSE(WriteBlobResponse.Companion::read),

    LIST_ENTITIES_REQUEST(ListEntitiesRequest.Companion::read),
    LIST_ENTITIES_RESPONSE(ListEntitiesResponse.Companion::read),

    CREATE_BLOB_REQUEST(CreateBlobRequest.Companion::read),
    CREATE_BLOB_RESPONSE(CreateBlobResponse.Companion::read),

    GET_BLOB_META_REQUEST(GetBlobMetaRequest.Companion::read),
    GET_BLOB_META_RESPONSE(GetBlobMetaResponse.Companion::read),

}