/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.route.Router
import zakadabar.stack.backend.route.RouterProvider
import zakadabar.stack.data.entity.EntityBo

class KtorRouterProvider : RouterProvider {

    override fun <T : EntityBo<T>> businessLogicRouter(businessLogic : EntityBusinessLogicBase<T>) : Router<T> {
        return KtorRouter(businessLogic)
    }

}