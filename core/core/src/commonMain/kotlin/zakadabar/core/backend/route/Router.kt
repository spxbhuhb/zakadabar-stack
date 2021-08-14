/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.route

import zakadabar.core.backend.authorize.Executor
import zakadabar.core.data.BaseBo
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.query.QueryBo
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