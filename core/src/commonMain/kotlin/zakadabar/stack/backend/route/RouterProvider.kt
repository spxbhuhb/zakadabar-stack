/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.route

import zakadabar.stack.backend.business.EntityBusinessLogicCommon
import zakadabar.stack.data.entity.EntityBo

interface RouterProvider {

    fun <T : EntityBo<T>> businessLogicRouter(businessLogic : EntityBusinessLogicCommon<T>) : Router<T>

}