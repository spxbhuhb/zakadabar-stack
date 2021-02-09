/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import zakadabar.stack.data.DtoBase

interface ActionDto<RESPONSE : DtoBase> : DtoBase {
    suspend fun execute(): RESPONSE
}

