/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.DtoBase


interface ActionBo<RESPONSE : BaseBo> : DtoBase, BaseBo {
    suspend fun execute(): RESPONSE
}

@Deprecated("EOL: 2021.6.30 - use ActionBo instead", ReplaceWith("ActionBo"))
interface ActionDto<RESPONSE : DtoBase> : ActionBo<RESPONSE>


