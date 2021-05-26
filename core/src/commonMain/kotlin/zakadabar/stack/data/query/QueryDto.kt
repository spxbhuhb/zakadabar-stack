/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import zakadabar.stack.data.BaseBo

interface QueryBo<RESULT> : BaseBo {
    suspend fun execute(): List<RESULT>
}