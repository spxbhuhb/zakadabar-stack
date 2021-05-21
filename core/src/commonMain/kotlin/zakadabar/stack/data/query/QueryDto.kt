/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.DtoBase

interface QueryBo<RESULT> : DtoBase, BaseBo {
    suspend fun execute(): List<RESULT>
}

@Deprecated("EOL: 2021.6.30 - use QueryBo instead", ReplaceWith("QueryBo"))
interface QueryDto<RESULT> : QueryBo<RESULT>
