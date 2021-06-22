/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend.testing

import kotlinx.coroutines.runBlocking
import zakadabar.lib.accounts.backend.install
import zakadabar.lib.accounts.data.LoginAction
import zakadabar.lib.accounts.data.SessionBo
import zakadabar.stack.RolesBase
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizerProvider
import zakadabar.stack.backend.server
import zakadabar.stack.backend.testing.TestCompanionBase
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId

/**
 * Helper class for unit tests with full authentication.
 *
 * @param   gracePeriodMillis   Grace period for ktor shutdown, default is 0.
 * @param   timeoutMillis       Ktor shutdown timeout, default is 2000.
 * @param   roles               Roles to use, defaults to [RolesBase].
 * @param   settings            Server settings file, defaults to "./template/test/stack.server.yaml".
 * @param   baseUrl             The URL on which the Ktor instance is available, defaults to `http://127.0.0.1:8888`.
 * @param   simpleProvider      When true (default), adds a [SimpleRoleAuthorizerProvider] with `all = roles.siteMember`.
 * @param   credentials         When not null, executes a login in [onAfterStarted]. Defaults to `"so" to "so"`.
 */
open class AuthTestCompanionBase(
    gracePeriodMillis : Long = 0,
    timeoutMillis : Long = 2000,
    roles : RolesBase = RolesBase(),
    settings : String = "./template/test/stack.server.yaml",
    baseUrl : String = "http://127.0.0.1:8888",
    val simpleProvider : Boolean = true,
    val credentials : Pair<String,String>? = "so" to "so"
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
                all = roles.siteMember
            }
        }
    }

    override fun onAfterStarted() {
        if (credentials != null) {
            runBlocking {
                LoginAction(credentials.first, Secret(credentials.second)).execute()
                session = SessionBo.read(EntityId("current"))
            }
        }
    }

}