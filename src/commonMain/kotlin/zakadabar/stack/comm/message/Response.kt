/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.message

import zakadabar.stack.comm.serialization.ResponseCode

interface Response : StackMessage {
    val responseCode: ResponseCode

    val ok: Boolean
        get() = (responseCode == ResponseCode.OK)

    val error: Boolean
        get() = (responseCode != ResponseCode.OK)
}