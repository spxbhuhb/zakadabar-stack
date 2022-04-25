/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.testing

import zakadabar.core.authorize.*
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.log.Slf4jLogger
import zakadabar.core.module.modules
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.Server
import zakadabar.core.server.server
import zakadabar.core.testing.TestCompanionBase.MockRoleBlProvider
import zakadabar.core.util.UUID

/**
 * Helper class for unit tests. Starts a Ktor server with an
 * test settings.
 *
 * Sets [CommConfig.baseUrl] to [baseUrl].
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

    val mockAnonymous = Executor(EntityId(0), UUID.NIL, true, emptyList(), emptyList())
    val mockLoggedIn = Executor(EntityId(0), UUID.NIL, false, emptyList(), emptyList())
    val mockSo = Executor(EntityId(0), UUID.NIL, false, listOf(EntityId(0)), listOf("security-officer"))

    open fun setup() {
        modules.logger = Slf4jLogger("modules") // replace the stdout logger with LOGBack
        modules.logger.info("working directory: ${System.getProperty("user.dir")}")

        server = Server("test")

        appRoles = roles

        if (allPublic) {
            modules += SimpleRoleAuthorizerProvider {
                all = PUBLIC
            }
        }

        if (mockRoleBlProvider) {
            modules += MockRoleBlProvider
        }

        CommConfig.global = CommConfig(baseUrl)

        addModules()

        server.main(arrayOf("--test", "--settings", settings))

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