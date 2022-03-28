/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig

interface QueryBo<RESULT : Any?> : BaseBo {

    suspend fun execute(): RESULT = execute(null, null)

    suspend fun execute(executor : Executor? = null, callConfig : CommConfig? = null): RESULT {
        throw NotImplementedError()
    }

}