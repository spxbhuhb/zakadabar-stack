/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.exceptions.Forbidden

/**
 * Implemented by authorizer classes. These are used by business logic
 * modules to authorize access.
 */
interface Authorizer<T : EntityBo<T>> {

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