/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import zakadabar.stack.data.BaseBo

interface ActionBo<RESPONSE : Any> : BaseBo {
    suspend fun execute(): RESPONSE
}
