/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.data.entity.EntityBo

/**
 * Provides a [SimpleRoleAuthorizer] for all BL modules.
 */
class SimpleRoleAuthorizerProvider() : AuthorizerProvider, BackendModule {

    private var initializer: (SimpleRoleAuthorizer<*>.() -> Unit)? = null

    constructor(initializer: SimpleRoleAuthorizer<*>.() -> Unit) : this() {
        this.initializer = initializer
    }

    override fun <T : EntityBo<T>> businessLogicAuthorizer(businessLogic : EntityBusinessLogicBase<T>) : Authorizer<T> {
        return SimpleRoleAuthorizer(initializer as SimpleRoleAuthorizer<T>.() -> Unit)
    }

}