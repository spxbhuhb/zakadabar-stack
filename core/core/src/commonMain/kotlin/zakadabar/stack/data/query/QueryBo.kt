/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data.query

import zakadabar.core.data.BaseBo

interface QueryBo<RESULT : Any?> : BaseBo {

    suspend fun execute(): RESULT

}