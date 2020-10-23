/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.backend.builtin.session

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.sessions.*
import zakadabar.stack.backend.BackendContext
import zakadabar.stack.util.Executor

class SessionAuthenticationProvider internal constructor(configuration: Configuration) :
    AuthenticationProvider(configuration) {

    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name)

}

fun Authentication.Configuration.session(name: String? = null) {

    val provider = AuthenticationProvider(SessionAuthenticationProvider.Configuration(name))

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->

        var session = call.sessions.get<StackSession>()

        if (session == null) {
            session = StackSession(BackendContext.anonymous.id.value)
            call.sessions.set(session)
        }

        context.principal(Executor(session.executorId))

    }

    register(provider)

}
