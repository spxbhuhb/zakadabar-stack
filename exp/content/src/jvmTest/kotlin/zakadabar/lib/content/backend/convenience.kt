/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import kotlinx.coroutines.runBlocking
import zakadabar.lib.accounts.data.LoginAction
import zakadabar.lib.accounts.data.SessionBo
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizerProvider
import zakadabar.stack.backend.server
import zakadabar.stack.data.CommBase
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId

open class TestCompanion {

    lateinit var session: SessionBo

    open fun setup(username : String?, password : String?) {
        server = Server("test")
        server += TestModule
        server.main(arrayOf("--test", "--settings", "./template/test/stack.server.yaml"))

        CommBase.baseUrl = "http://127.0.0.1:8888"

        if (username != null && password != null) {
            runBlocking {
                LoginAction(username, Secret(password)).execute()
                session = SessionBo.read(EntityId("current"))
            }
        }
    }

    open fun teardown() {
        server.ktorServer.stop(1000, 10000)
    }

}

object TestModule : BackendModule {

    override fun onModuleLoad() {
        zakadabar.lib.accounts.backend.install()
        zakadabar.lib.i18n.backend.install()
        install()

        server += SimpleRoleAuthorizerProvider {
            all = StackRoles.siteMember
        }
    }

}