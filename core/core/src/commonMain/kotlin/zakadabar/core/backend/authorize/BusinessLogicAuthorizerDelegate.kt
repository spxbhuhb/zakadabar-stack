/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.authorize

import zakadabar.core.backend.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.module.modules
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import zakadabar.core.data.UNINITIALIZED

class BusinessLogicAuthorizerDelegate<T : BaseBo>(
    var build: (BusinessLogicAuthorizer<T>.() -> Unit)? = null
) : ReadOnlyProperty<BusinessLogicCommon<T>, BusinessLogicAuthorizer<T>> {

    private var authorizer: Any = UNINITIALIZED

    @Suppress("UNCHECKED_CAST") // this is how "lazy" does it...
    override operator fun getValue(thisRef: BusinessLogicCommon<T>, property: KProperty<*>): BusinessLogicAuthorizer<T> {
        if (authorizer === UNINITIALIZED) {
            val provider = modules.firstOrNull<AuthorizerProvider>()
            authorizer = if (provider == null) {
                modules.logger.warn("Cannot find authorizer provider, requests will be denied!")
                EmptyAuthorizer()
            } else {
                provider
                    .businessLogicAuthorizer(thisRef)
                    .apply { build?.let { it() }  }
            }
        }
        return authorizer as BusinessLogicAuthorizer<T>
    }

}
