/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.authorize

import zakadabar.core.backend.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.module.modules
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


interface AuthorizerProvider {

    /**
     * Create a [BusinessLogicAuthorizer] for the given business logic.
     */
    fun <T : BaseBo> businessLogicAuthorizer(businessLogic: BusinessLogicCommon<T>): BusinessLogicAuthorizer<T>

}