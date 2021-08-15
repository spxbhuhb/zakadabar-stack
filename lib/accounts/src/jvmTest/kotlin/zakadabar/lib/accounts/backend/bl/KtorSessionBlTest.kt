/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend.bl

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.lib.accounts.backend.testing.AuthTestCompanionBase
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.lib.accounts.data.LoginAction
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.lib.accounts.data.httpClientWithAuth
import zakadabar.core.comm.CommBase
import zakadabar.core.comm.CommBase.Companion.httpClient
import zakadabar.core.data.Secret

class KtorSessionBlTest {

    companion object : AuthTestCompanionBase(
        credentials = null
    ) {

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testBasic() {
        runBlocking {
            CommBase.client = httpClientWithAuth("so", "so")
            AccountPrivateBo.all()
        }
    }

    @Test
    fun testSession() {
        runBlocking {
            CommBase.client = httpClient()
            LoginAction("so", Secret("so")).execute()
            AccountPrivateBo.all()
            LogoutAction().execute()
        }
    }
}