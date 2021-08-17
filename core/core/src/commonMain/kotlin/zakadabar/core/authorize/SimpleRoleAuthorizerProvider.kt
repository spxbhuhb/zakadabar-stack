/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.module.CommonModule

/**
 * Provides a [SimpleRoleAuthorizer] for all BL modules.
 */
class SimpleRoleAuthorizerProvider() : AuthorizerProvider, CommonModule {

    private var initializer: (SimpleRoleAuthorizer<*>.() -> Unit)? = null

    constructor(initializer: SimpleRoleAuthorizer<*>.() -> Unit) : this() {
        this.initializer = initializer
    }

    override fun <T : BaseBo> businessLogicAuthorizer(businessLogic : BusinessLogicCommon<T>) : BusinessLogicAuthorizer<T> {
        return SimpleRoleAuthorizer(initializer as SimpleRoleAuthorizer<T>.() -> Unit)
    }

}