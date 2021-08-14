/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo


interface AuthorizerProvider {

    /**
     * Create a [BusinessLogicAuthorizer] for the given business logic.
     */
    fun <T : BaseBo> businessLogicAuthorizer(businessLogic: BusinessLogicCommon<T>): BusinessLogicAuthorizer<T>

}