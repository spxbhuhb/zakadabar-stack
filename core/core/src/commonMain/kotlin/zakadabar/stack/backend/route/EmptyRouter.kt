/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.route

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.query.QueryBo
import kotlin.reflect.KClass

/**
 * Does not add any endpoints.
 */
class EmptyRouter<T : EntityBo<T>> : Router<T> {

    override var qualifier = "empty"

    override fun installRoutes(context: Any) {

    }

    override fun <RQ : ActionBo<RS>, RS : Any> action(actionClass: KClass<RQ>, actionFunc: (Executor, RQ) -> RS) {
        throw IllegalArgumentException("EmptyRouter cannot route actions")
    }

    override fun <RQ : QueryBo<RS>, RS : Any> query(queryClass: KClass<RQ>, queryFunc: (Executor, RQ) -> RS) {
        throw IllegalArgumentException("EmptyRouter cannot route queries")
    }

}