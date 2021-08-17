/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.business.EntityBusinessLogicCommon
import zakadabar.core.route.BusinessLogicRouter
import zakadabar.core.route.RouterProvider
import zakadabar.core.data.BaseBo

class KtorRouterProvider : RouterProvider {

    override fun <T : BaseBo> businessLogicRouter(businessLogic: BusinessLogicCommon<T>): BusinessLogicRouter<T> {
        return if (businessLogic is EntityBusinessLogicCommon) {
            @Suppress("UNCHECKED_CAST") // types are actually OK here, but we have to erase them
            entityRouter(businessLogic) as BusinessLogicRouter<T>
        } else {
            KtorRouter(businessLogic)
        }
    }

    private fun entityRouter(entityBusinessLogic: EntityBusinessLogicCommon<*>) =
        KtorEntityRouter(entityBusinessLogic)

}