/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.lib.accounts.backend.testing.AuthTestCompanionBase
import zakadabar.stack.backend.server
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AccountBlTest {

    companion object : AuthTestCompanionBase(
        roles = Roles
    ) {

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testInitializeDb() = runBlocking {
        val accountBl = server.first<AccountPrivateBl>()
        val roleBl = server.first<RoleBl>()

        transaction {
            roleBl.getByName(Roles.securityOfficer)
            roleBl.getByName(Roles.myRole)

            val so = accountBl.byName("so")
            val anonymous = accountBl.byName("anonymous")

            val soRoles = accountBl.roles(so.id).map { it.second }
            assertEquals(1, soRoles.size)
            assertTrue(Roles.securityOfficer in soRoles)

            assertEquals(0, accountBl.roles(anonymous.id).size)
        }
    }

}