/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.testing

import zakadabar.stack.authorize.AppRolesBase
import zakadabar.stack.authorize.appRoles
import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.authorize.RoleBlProvider
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizerProvider
import zakadabar.stack.backend.server
import zakadabar.stack.backend.testing.TestCompanionBase.MockRoleBlProvider
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.CommBase
import zakadabar.stack.data.entity.EntityId

/**
 * Helper class for unit tests. Starts a Ktor server with an
 * test settings.
 *
 * Sets [CommBase.baseUrl] to [baseUrl].
 *
 * @param   gracePeriodMillis   Grace period for ktor shutdown, default is 0.
 * @param   timeoutMillis       Ktor shutdown timeout, default is 2000.
 * @param   allPublic           When true (default), a [SimpleRoleAuthorizerProvider] is added to the server
 *                              with `all=PUBLIC`.
 * @param   mockRoleBlProvider  When true (default), a [MockRoleBlProvider] is added to the server.
 * @param   roles               Roles to use, defaults to [AppRolesBase].
 * @param   settings            Server settings file, defaults to "./template/test/stack.server.yaml".
 * @param   baseUrl             The URL on which the Ktor instance is available, defaults to `http://127.0.0.1:8888`.
 */
open class TestCompanionBase(
    val gracePeriodMillis : Long = 0,
    val timeoutMillis : Long = 2000,
    val allPublic : Boolean = true,
    val mockRoleBlProvider : Boolean = true,
    val roles : AppRolesBase = AppRolesBase(),
    val settings : String = "./template/test/stack.server.yaml",
    val baseUrl : String = "http://127.0.0.1:8888"
) {

    open fun addModules() {

    }

    open fun onAfterStarted() {

    }

    open fun setup() {
        server = Server("test")

        appRoles = roles

        if (allPublic) {
            server += SimpleRoleAuthorizerProvider {
                all = PUBLIC
            }
        }

        if (mockRoleBlProvider) {
            server += MockRoleBlProvider
        }

        addModules()

        server.main(arrayOf("--test", "--settings", settings))

        CommBase.baseUrl = baseUrl

        onAfterStarted()
    }

    open fun teardown() {
        server.shutdown()
    }

    object MockRoleBlProvider : RoutedModule, RoleBlProvider {

        private val roleIds = mutableMapOf<String,EntityId<BaseBo>>()

        override fun onModuleLoad() {
            appRoles.map.forEach {
                roleIds[it.value] = EntityId(roleIds.size + 1L)
            }
        }

        override fun getByName(name: String): EntityId<BaseBo> =
            roleIds[name] ?: throw NoSuchElementException()
    }

}