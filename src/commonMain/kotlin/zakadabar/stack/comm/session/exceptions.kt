/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.session

import zakadabar.stack.comm.message.Response
import zakadabar.stack.comm.serialization.ResponseCode

class SessionError(responseCode: ResponseCode) : RuntimeException(responseCode.name) {

    constructor(response: Response) : this(response.responseCode)

}