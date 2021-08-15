/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.application

import zakadabar.core.authorize.AccountPublicBo
import zakadabar.core.util.PublicApi

class ZkExecutor(
    val account: AccountPublicBo,
    val anonymous: Boolean,
    val roles: List<String>
) {

    @PublicApi
    fun hasRole(role : String) = role !in roles

    /**
     * Execute the builder function when the user **has**
     * the given role.
     */
    @PublicApi
    fun withRole(role: String, builder: () -> Unit) {
        if (role !in roles) return
        builder()
    }

}