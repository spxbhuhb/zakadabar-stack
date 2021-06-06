/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.moduleLogger
import zakadabar.stack.backend.server
import zakadabar.stack.data.entity.EntityBo
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private object UNINITIALIZED

class AuthorizerDelegate<T : EntityBo<T>> : ReadOnlyProperty<EntityBusinessLogicBase<T>, Authorizer<T>> {

    private var authorizer: Any = UNINITIALIZED

    @Suppress("UNCHECKED_CAST") // this is how "lazy" does it...
    override operator fun getValue(thisRef: EntityBusinessLogicBase<T>, property: KProperty<*>): Authorizer<T> {
        if (authorizer === UNINITIALIZED) {
            val provider = server.modules.firstOrNull<AuthorizerProvider>()
            authorizer = if (provider == null) {
                moduleLogger.warn("Cannot find authorizer provider, requests will be denied!")
                EmptyAuthorizer()
            } else {
                provider.businessLogicAuthorizer(thisRef)
            }
        }
        return authorizer as Authorizer<T>
    }

}

interface AuthorizerProvider {

    /**
     * Create an [Authorizer] for the given business logic.
     */
    fun <T : EntityBo<T>> businessLogicAuthorizer(businessLogic: EntityBusinessLogicBase<T>): Authorizer<T>


}