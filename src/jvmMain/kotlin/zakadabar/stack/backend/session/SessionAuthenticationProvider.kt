/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.backend.session

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
