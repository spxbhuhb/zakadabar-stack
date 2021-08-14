/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.validate

import zakadabar.core.authorize.Executor
import zakadabar.core.data.BaseBo
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.query.QueryBo

/**
 * Implemented by business object validators used by business logic modules
 * to validate incoming business objects.
 */
interface BusinessLogicValidator<T : BaseBo> {

    fun validateCreate(executor: Executor, bo : T) {

    }

    fun validateUpdate(executor: Executor, bo : T) {

    }

    fun validateAction(executor : Executor, bo : ActionBo<*>) {

    }

    fun validateQuery(executor : Executor, bo : QueryBo<*>) {

    }

}