/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.route

import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo

interface RouterProvider {

    fun <T : BaseBo> businessLogicRouter(businessLogic : BusinessLogicCommon<T>) : Router<T>

}