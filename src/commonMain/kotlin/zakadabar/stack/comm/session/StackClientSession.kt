/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.session

import zakadabar.stack.comm.message.CloseStackSessionRequest
import zakadabar.stack.comm.message.OpenStackSessionRequest
import zakadabar.stack.comm.message.StackMessage
import zakadabar.stack.comm.serialization.StackInputCommArray
import zakadabar.stack.comm.serialization.StackOutputCommArray

/**
 * Base class for transfer sessions on the client side. The connection is opened at the time
 * the instance is created.
 */
class StackClientSession(
    host: String,
    port: Int,
    path: String
) : ClientCommSession<StackMessage, StackMessage>(
    host = host,
    port = port,
    path = path,
    openRequest = { OpenStackSessionRequest(authToken = "") },
    onOpenResponse = { },
    closeRequest = { CloseStackSessionRequest() },
    write = { message -> StackOutputCommArray().write(message) },
    read = { byteArray -> StackInputCommArray(byteArray).readMessage() }
)