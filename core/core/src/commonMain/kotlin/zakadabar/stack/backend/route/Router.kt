/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.route

import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.query.QueryBo
import kotlin.reflect.KClass

/**
 * Creates routes incoming requests to the appripriate BL processing method.
 */
interface Router<T : BaseBo> {

    /**
     * In the structure `/api/{namespace}/{qualifier}/...` this is the qualifier.
     * Entity BLs use `entity`, blob BLs use `blob/meta` (will change to `blob\structured`).
     */
    var qualifier : String

    fun installRoutes(context: Any)

    fun <RQ : ActionBo<RS>, RS : Any?> action(actionClass: KClass<RQ>, actionFunc: (Executor, RQ) -> RS)

    fun prepareAction(
        actionType : String,
        actionData : String
    ) : Pair<(Executor, BaseBo) -> Any?,BaseBo>

    fun <RQ : QueryBo<RS>, RS : Any?> query(queryClass: KClass<RQ>, queryFunc: (Executor, RQ) -> RS)

}