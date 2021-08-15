/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend.testing

import kotlinx.coroutines.runBlocking
import zakadabar.lib.accounts.backend.install
import zakadabar.lib.accounts.data.LoginAction
import zakadabar.lib.accounts.data.SessionBo
import zakadabar.core.authorize.AppRolesBase
import zakadabar.core.authorize.SimpleRoleAuthorizer.Companion.LOGGED_IN
import zakadabar.core.authorize.SimpleRoleAuthorizerProvider
import zakadabar.core.server.server
import zakadabar.core.testing.TestCompanionBase
import zakadabar.core.data.Secret
import zakadabar.core.data.EntityId
import zakadabar.core.util.PublicApi

/**
 * Helper class for unit tests with full authentication.
 *
 * @param   gracePeriodMillis   Grace period for ktor shutdown, default is 0.
 * @param   timeoutMillis       Ktor shutdown timeout, default is 2000.
 * @param   roles               Roles to use, defaults to [AppRolesBase].
 * @param   settings            Server settings file, defaults to "./template/test/stack.server.yaml".
 * @param   baseUrl             The URL on which the Ktor instance is available, defaults to `http://127.0.0.1:8888`.
 * @param   simpleProvider      When true (default), adds a [SimpleRoleAuthorizerProvider] with `all = roles.siteMember`.
 * @param   credentials         When not null, executes a login in [onAfterStarted]. Defaults to `"so" to "so"`.
 */
@PublicApi
open class AuthTestCompanionBase(
    gracePeriodMillis : Long = 0,
    timeoutMillis : Long = 2000,
    roles : AppRolesBase = AppRolesBase(),
    settings : String = "./template/test/stack.server.yaml",
    baseUrl : String = "http://127.0.0.1:8888",
    open val simpleProvider : Boolean = true,
    open val credentials : Pair<String,String>? = "so" to "so"
) : TestCompanionBase(
    gracePeriodMillis = gracePeriodMillis,
    timeoutMillis = timeoutMillis,
    allPublic = false,
    mockRoleBlProvider = false,
    roles = roles,
    settings = settings,
    baseUrl = baseUrl
) {

    /**
     * The session when [credentials] is not null and the automatic login
     * has been executed in [onAfterStarted].
     */
    lateinit var session : SessionBo

    override fun addModules() {
        install(roles)

        if (simpleProvider) {
            server += SimpleRoleAuthorizerProvider {
                all = LOGGED_IN
            }
        }
    }

    override fun onAfterStarted() {
        credentials?.let {
            runBlocking {
                LoginAction(it.first, Secret(it.second)).execute()
                session = SessionBo.read(EntityId("current"))
            }
        }
    }

}