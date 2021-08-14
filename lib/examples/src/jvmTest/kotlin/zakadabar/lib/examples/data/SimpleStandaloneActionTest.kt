/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.data

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.lib.examples.backend.data.SimpleStandaloneActionBl
import zakadabar.core.server.Server
import zakadabar.core.authorize.Executor
import zakadabar.core.server.server
import zakadabar.core.data.BaseBo
import zakadabar.core.data.CommBase
import zakadabar.core.data.action.ActionBo
import kotlin.test.assertEquals

class SimpleStandaloneActionTest {

    companion object {

        @BeforeClass
        @JvmStatic
        fun setup() {
            server = Server("test")
            server += object : SimpleStandaloneActionBl() {
                override val authorizer = object : BusinessLogicAuthorizer<BaseBo> {
                    override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
                        // allow the action
                    }
                }
            }
            server.main(arrayOf("--test"))
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            server.ktorServer.stop(1000, 10000)
        }
    }

    @Test
    fun testSimpleStandaloneAction() {
        runBlocking {
            CommBase.baseUrl = "http://127.0.0.1:8888"
            assertEquals("test action", SimpleStandaloneAction(name = "ize").execute().reason)
        }
    }
}