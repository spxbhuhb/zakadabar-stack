/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.business

import zakadabar.core.backend.authorize.Executor
import zakadabar.core.data.BaseBo

interface QueryBusinessLogicWrapper {
    fun queryWrapper(executor: Executor, func: (Executor, BaseBo) -> Any?, bo: BaseBo): Any?
}