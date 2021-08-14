/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.route

import zakadabar.core.backend.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo

interface RouterProvider {

    fun <T : BaseBo> businessLogicRouter(businessLogic : BusinessLogicCommon<T>) : Router<T>

}