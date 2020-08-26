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