/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.ktor

import zakadabar.core.backend.business.BusinessLogicCommon
import zakadabar.core.backend.business.EntityBusinessLogicCommon
import zakadabar.core.backend.route.Router
import zakadabar.core.backend.route.RouterProvider
import zakadabar.core.data.BaseBo

class KtorRouterProvider : RouterProvider {

    override fun <T : BaseBo> businessLogicRouter(businessLogic: BusinessLogicCommon<T>): Router<T> {
        return if (businessLogic is EntityBusinessLogicCommon) {
            @Suppress("UNCHECKED_CAST") // types are actually OK here, but we have to erase them
            entityRouter(businessLogic) as Router<T>
        } else {
            KtorRouter(businessLogic)
        }
    }

    private fun entityRouter(entityBusinessLogic: EntityBusinessLogicCommon<*>) =
        KtorEntityRouter(entityBusinessLogic)

}