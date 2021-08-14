/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.authorize

import zakadabar.core.data.BaseBo
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.query.QueryBo
import zakadabar.core.exceptions.Forbidden

/**
 * Implemented by authorizer classes. These are used by business logic
 * modules to authorize access.
 */
interface Authorizer<T : BaseBo> {

    fun onModuleStart() {

    }

    fun authorizeList(executor: Executor) {
        throw Forbidden()
    }

    fun authorizeRead(executor: Executor, entityId: EntityId<T>) {
        throw Forbidden()
    }

    fun authorizeCreate(executor: Executor, entity: T) {
        throw Forbidden()
    }

    fun authorizeUpdate(executor: Executor, entity: T) {
        throw Forbidden()
    }

    fun authorizeDelete(executor: Executor, entityId: EntityId<T>) {
        throw Forbidden()
    }

    fun authorizeAction(executor : Executor, actionBo : ActionBo<*>)  {
        throw Forbidden()
    }

    fun authorizeQuery(executor : Executor, queryBo : QueryBo<*>)  {
        throw Forbidden()
    }

}