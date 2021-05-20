/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.backend.ktor.session

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.sessions.*
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.session.LoginTimeout
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.util.Executor

class SessionAuthenticationProvider internal constructor(configuration: Configuration) :
    AuthenticationProvider(configuration) {

    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name)

}

fun Authentication.Configuration.session(name: String? = null) {

    val provider = AuthenticationProvider(SessionAuthenticationProvider.Configuration(name))

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->

        call.sessions.get<StackSession>()?.let {
            context.principal(Executor(it.account, it.roleIds, it.roleNames))
            return@intercept
        }

        val session = StackSession(LongRecordId(Server.anonymous.id.toLong()), emptyList(), emptyList())
        call.sessions.set(session)
        context.principal(Executor(session.account, session.roleIds, session.roleNames))

        if (call.attributes.getOrNull(LoginTimeoutKey) == true) {
            throw LoginTimeout()
        }
    }

    register(provider)
}
