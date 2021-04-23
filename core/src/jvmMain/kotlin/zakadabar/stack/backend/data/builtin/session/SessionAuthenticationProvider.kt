/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.backend.data.builtin.session

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.sessions.*
import zakadabar.stack.backend.Server
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
            session = StackSession(Server.anonymous.id, emptyList(), emptyList())
            call.sessions.set(session)

            // When there is a STACK_SESSION cookie but the session instance was null
            // the session is expired. In this case the server responds with
            // 440 Login Timeout, so the client can perform a re-login.

            val cookie = call.request.cookies["STACK_SESSION"]
            if (cookie != null) throw LoginTimeout()
        }

        context.principal(Executor(session.account, session.roleIds, session.roleNames))
    }

    register(provider)

}
