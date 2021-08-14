/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.route

import zakadabar.core.authorize.Executor
import zakadabar.core.data.BaseBo
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.query.QueryBo
import kotlin.reflect.KClass

/**
 * Does not add any endpoints.
 */
class EmptyRouter<T : EntityBo<T>> : BusinessLogicRouter<T> {

    override var qualifier = "empty"

    override fun installRoutes(context: Any) {

    }

    override fun <RQ : ActionBo<RS>, RS : Any?> action(actionClass: KClass<RQ>, actionFunc: (Executor, RQ) -> RS) {
        throw IllegalArgumentException("EmptyRouter cannot route actions")
    }

    override fun prepareAction(
        actionType : String,
        actionData : String
    ) : Pair<(Executor, BaseBo) -> Any?, BaseBo> {
        throw IllegalArgumentException("EmptyRouter cannot route actions")
    }

    override fun <RQ : QueryBo<RS>, RS : Any?> query(queryClass: KClass<RQ>, queryFunc: (Executor, RQ) -> RS) {
        throw IllegalArgumentException("EmptyRouter cannot route queries")
    }

}