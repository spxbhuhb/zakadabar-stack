/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.route

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass

/**
 * Creates an audit record of the operation.
 */
interface Router<T : EntityBo<T>> {

    fun installRoutes(context: Any)

    fun <RQ : ActionBo<RS>, RS : BaseBo> action(actionClass: KClass<RQ>, actionFunc: (Executor, RQ) -> RS)

    fun <RQ : Any, RS : Any> query(queryClass: KClass<RQ>, queryFunc: (Executor, RQ) -> RS)

}