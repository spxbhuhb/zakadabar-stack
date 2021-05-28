/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.validate

import io.ktor.features.*
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.query.QueryBo

/**
 * Validates the business object by its schema using `isValid` of `BoSchema`.
 * Throws BadRequestException if the BO is invalid.
 */
class SchemaValidator<T : BaseBo> : Validator<T> {

    override fun validateCreate(executor: Executor, bo : T) {
        if (! bo.isValid) throw BadRequestException("invalid request data")
    }

    override fun validateUpdate(executor: Executor, bo : T) {
        if (! bo.isValid) throw BadRequestException("invalid request data")
    }

    override fun validateAction(executor : Executor, bo : ActionBo<*>) {
        if (! bo.isValid) throw BadRequestException("invalid request data")
    }

    override fun validateQuery(executor : Executor, bo : QueryBo<*>) {
        if (! bo.isValid) throw BadRequestException("invalid request data")
    }

}