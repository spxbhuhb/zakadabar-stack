/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.query.QueryBo
import zakadabar.core.exception.Forbidden

/**
 * Allows everything when the companion field is set to true.
 *
 * Use with care, intended for special implementations,
 * like the examples on site which are prepared for public access.
 *
 * Throws [Forbidden] when rejected.
 */
open class UnsafeAuthorizer<T : EntityBo<T>>() : BusinessLogicAuthorizer<T> {

    companion object {
        var enabled = false
    }

    override fun authorizeList(executor: Executor) {
        if (! enabled) throw Forbidden()
    }

    override fun authorizeRead(executor: Executor, entityId: EntityId<T>) {
        if (! enabled) throw Forbidden()
    }

    override fun authorizeCreate(executor: Executor, entity: T) {
        if (! enabled) throw Forbidden()
    }

    override fun authorizeUpdate(executor: Executor, entity: T) {
        if (! enabled) throw Forbidden()
    }

    override fun authorizeDelete(executor: Executor, entityId: EntityId<T>) {
        if (! enabled) throw Forbidden()
    }

    override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
        if (! enabled) throw Forbidden()
    }

    override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
        if (! enabled) throw Forbidden()
    }

}
