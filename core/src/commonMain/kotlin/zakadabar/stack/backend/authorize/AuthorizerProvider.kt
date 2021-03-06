/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.backend.business.EntityBusinessLogicCommon
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.module.moduleLogger
import zakadabar.stack.module.modules
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private object UNINITIALIZED

class AuthorizerDelegate<T : EntityBo<T>>(
    var build: (Authorizer<T>.() -> Unit)? = null
) : ReadOnlyProperty<EntityBusinessLogicCommon<T>, Authorizer<T>> {

    private var authorizer: Any = UNINITIALIZED

    @Suppress("UNCHECKED_CAST") // this is how "lazy" does it...
    override operator fun getValue(thisRef: EntityBusinessLogicCommon<T>, property: KProperty<*>): Authorizer<T> {
        if (authorizer === UNINITIALIZED) {
            val provider = modules.firstOrNull<AuthorizerProvider>()
            authorizer = if (provider == null) {
                moduleLogger.warn("Cannot find authorizer provider, requests will be denied!")
                EmptyAuthorizer()
            } else {
                provider
                    .businessLogicAuthorizer(thisRef)
                    .apply { build?.let { it() }  }
            }
        }
        return authorizer as Authorizer<T>
    }

}

interface AuthorizerProvider {

    /**
     * Create an [Authorizer] for the given business logic.
     */
    fun <T : EntityBo<T>> businessLogicAuthorizer(businessLogic: EntityBusinessLogicCommon<T>): Authorizer<T>


}