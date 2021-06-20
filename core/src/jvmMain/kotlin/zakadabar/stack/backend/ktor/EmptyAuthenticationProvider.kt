/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.backend.ktor

import io.ktor.auth.*
import zakadabar.stack.data.entity.EntityId

class EmptyAuthenticationProvider internal constructor(configuration: Configuration) : AuthenticationProvider(configuration) {
    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name)
}

fun Authentication.Configuration.configureEmpty(name: String? = null) {

    val provider = AuthenticationProvider(EmptyAuthenticationProvider.Configuration(name))

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        context.principal(KtorExecutor(EntityId("anonymous"), true, emptyList(), emptyList(), ""))
    }

    register(provider)
}
