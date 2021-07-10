/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.backend.business.EntityBusinessLogicCommon
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.module.CommonModule

/**
 * Provides a [SimpleRoleAuthorizer] for all BL modules.
 */
class SimpleRoleAuthorizerProvider() : AuthorizerProvider, CommonModule {

    private var initializer: (SimpleRoleAuthorizer<*>.() -> Unit)? = null

    constructor(initializer: SimpleRoleAuthorizer<*>.() -> Unit) : this() {
        this.initializer = initializer
    }

    override fun <T : EntityBo<T>> businessLogicAuthorizer(businessLogic : EntityBusinessLogicCommon<T>) : Authorizer<T> {
        return SimpleRoleAuthorizer(initializer as SimpleRoleAuthorizer<T>.() -> Unit)
    }

}