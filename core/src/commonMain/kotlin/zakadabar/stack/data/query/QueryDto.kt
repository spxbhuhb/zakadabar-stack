/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import zakadabar.stack.data.DtoBase

interface QueryDto<RESULT> : DtoBase {
    suspend fun execute(): List<RESULT>
}

