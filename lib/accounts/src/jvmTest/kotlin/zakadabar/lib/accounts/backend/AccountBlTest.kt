/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.stack.backend.server
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AccountBlTest {

    companion object : TestCompanion() {

        @BeforeClass
        @JvmStatic
        fun setup() = super.setup("so", "so")

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
            roleBl.getByName(Roles.siteAdmin)
            roleBl.getByName(Roles.siteMember)
            roleBl.getByName(Roles.myRole)

            val so = accountBl.byName("so")
            val anonymous = accountBl.byName("anonymous")

            val soRoles = accountBl.roles(so.id).map { it.second }
            assertEquals(3, soRoles.size)
            assertTrue(Roles.securityOfficer in soRoles)
            assertTrue(Roles.siteAdmin in soRoles)
            assertTrue(Roles.siteMember in soRoles)

            val anonymousRoles = accountBl.roles(anonymous.id).map { it.second }
            assertEquals(0, anonymousRoles.size)
        }
    }

}