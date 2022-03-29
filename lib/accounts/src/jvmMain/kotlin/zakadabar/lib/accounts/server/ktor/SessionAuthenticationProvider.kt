/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.server.ktor

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.core.authorize.AccountBlProvider
import zakadabar.core.data.Secret
import zakadabar.core.module.module
import zakadabar.core.server.ktor.KtorExecutor
import zakadabar.core.server.server
import zakadabar.lib.accounts.business.KtorSessionBl
import zakadabar.lib.accounts.data.LoginAction

class SessionAuthenticationProvider internal constructor(configuration: Configuration) : AuthenticationProvider(configuration) {
    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name)
}

fun Authentication.Configuration.configureSession(name: String? = null) {

    val sessionBl by module<KtorSessionBl>()
    val accountBl by module<AccountBlProvider>()

    val executor = accountBl.anonymousV2().let {
        KtorExecutor(it.accountId, it.accountUuid, true, emptyList(), emptyList())
    }

    val provider = AuthenticationProvider(SessionAuthenticationProvider.Configuration(name))

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->

        // when there is a session, use it

        call.sessions.get<StackSession>()?.let {
            context.principal(KtorExecutor(it.account, it.accountUuid, it.anonymous, it.roleIds, it.roleNames))
            return@intercept
        }

        // when there are credentials in the request, authenticate by those credentials

        val credentials = call.request.basicAuthenticationCredentials(Charsets.UTF_8)

        credentials?.let {
            transaction {
                val session = sessionBl.login(executor, LoginAction(credentials.name, Secret(credentials.password)))
                context.principal(KtorExecutor(session.account, session.accountUuid, session.anonymous, session.roleIds, session.roleNames))
            }
            return@intercept
        }

        val anonymous = accountBl.anonymousV2()
        val session = StackSession(anonymous.accountId, anonymous.accountUuid, true, emptyList(), emptyList())

        call.sessions.set(session)
        context.principal(KtorExecutor(session.account, session.accountUuid, session.anonymous, session.roleIds, session.roleNames))

        if (call.attributes.getOrNull(LoginTimeoutKey) == true) {
            server.onLoginTimeout(call)
        }
    }

    register(provider)
}
