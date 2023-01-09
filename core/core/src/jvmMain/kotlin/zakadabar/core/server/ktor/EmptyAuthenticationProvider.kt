/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.server.ktor

import io.ktor.server.auth.*
import zakadabar.core.data.EntityId
import zakadabar.core.util.UUID

class EmptyAuthenticationProvider internal constructor(configuration: Config) : AuthenticationProvider(configuration) {
    class Configuration internal constructor(name: String?) : Config(name)

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        context.principal(KtorExecutor(EntityId("anonymous"), UUID.NIL, true, emptySet(), emptySet(), emptySet(), emptySet()))
    }
}

fun AuthenticationConfig.configureEmpty(name: String? = null) {
    register(EmptyAuthenticationProvider(EmptyAuthenticationProvider.Configuration(name)))
}
