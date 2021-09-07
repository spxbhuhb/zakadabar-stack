/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.route

import zakadabar.core.authorize.Executor
import zakadabar.core.data.ActionBo
import zakadabar.core.data.BaseBo
import zakadabar.core.data.QueryBo
import kotlin.reflect.KClass

/**
 * Routes incoming requests to the appropriate BL processing method.
 */
interface BusinessLogicRouter<T : BaseBo> {

    /**
     * In the structure `/api/{namespace}/{qualifier}/...` this is the qualifier.
     * Entity BLs use `entity`, blob BLs use `blob/meta` (will change to `blob\structured`).
     */
    var qualifier : String

    fun installRoutes(context: Any)

    fun <RQ : ActionBo<RS>, RS : Any?> action(actionClass: KClass<RQ>, actionFunc: (Executor, RQ) -> RS)

    fun <RQ : QueryBo<RS>, RS : Any?> query(queryClass: KClass<RQ>, queryFunc: (Executor, RQ) -> RS)
    
    fun prepareAction(
        actionType : String,
        actionData : String
    ) : Pair<(Executor, BaseBo) -> Any?,BaseBo>

}