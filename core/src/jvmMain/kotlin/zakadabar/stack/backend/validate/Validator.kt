/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.validate

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.util.Executor

/**
 * Implemented by business object validators used by business logic modules
 * to validate incoming business objects.
 */
interface Validator<T : BaseBo> {

    fun validateCreate(executor: Executor, bo : T) {

    }

    fun validateUpdate(executor: Executor, bo : T) {

    }

    fun validateAction(executor : Executor, bo : ActionBo<*>) {

    }

    fun validateQuery(executor : Executor, bo : QueryBo<*>) {

    }

}