/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.validate

import zakadabar.core.backend.authorize.Executor
import zakadabar.core.data.BaseBo
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.query.QueryBo
import zakadabar.core.exceptions.BadRequest

/**
 * Validates the business object by its schema using `isValid` of `BoSchema`.
 * Throws BadRequestException if the BO is invalid.
 */
class SchemaValidator<T : BaseBo> : BusinessLogicValidator<T> {

    override fun validateCreate(executor: Executor, bo : T) {
        validate(bo, true)
    }

    override fun validateUpdate(executor: Executor, bo : T) {
        validate(bo)
    }

    override fun validateAction(executor : Executor, bo : ActionBo<*>) {
        validate(bo)
    }

    override fun validateQuery(executor : Executor, bo : QueryBo<*>) {
        validate(bo)
    }

    fun validate(bo : BaseBo, allowEmptyId : Boolean = false) {
        val report = bo.schema().validate(allowEmptyId)
        if (report.fails.isNotEmpty()) throw BadRequest(report)
    }
}