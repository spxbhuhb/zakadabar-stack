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
import zakadabar.lib.accounts.data.*
import zakadabar.stack.backend.server
import zakadabar.stack.backend.setting.setting
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
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
    fun testInitializeDb() = transaction {
        val accountBl = server.first<AccountPrivateBl>()
        val roleBl = server.first<RoleBl>()

        roleBl.getByName(Roles.securityOfficer)
        roleBl.getByName(Roles.myRole)

        val so = accountBl.byName("so")
        val anonymous = accountBl.byName("anonymous")

        val soRoles = accountBl.roles(so.id).map { it.second }
        assertEquals(1, soRoles.size)
        assertTrue(Roles.securityOfficer in soRoles)

        assertEquals(0, accountBl.roles(anonymous.id).size)
    }

    @Test
    fun testCreateAccount() = runBlocking {

        val (accountName, password, bo) = createAccount()

        assertEquals(accountName, bo.accountName)
        assertNull(bo.credentials)

        assertTrue(LoginAction(accountName, password).execute().success)
        assertTrue(LogoutAction().execute().success)
    }


    @Test
    fun testLockedAccount() = runBlocking {

        val (accountName, password, bo) = createAccount()
        assertTrue(LoginAction(accountName, password).execute().success)
        assertTrue(LogoutAction().execute().success)

        assertTrue(LoginAction(accountName, password).execute().success)

        LoginAction("so", Secret("so")).execute()
        UpdateAccountLocked(bo.id, true).execute()
        LogoutAction().execute()

        assertFalse(LoginAction(accountName, password).execute().success)

        LoginAction("so", Secret("so")).execute()
        UpdateAccountLocked(bo.id, false).execute()
        LogoutAction().execute()

        assertTrue(LoginAction(accountName, password).execute().success)
    }

    @Test
    fun testAutoLock() = runBlocking {

        val (accountName) = createAccount()

        val settings by setting<ModuleSettings>()

        for (i in 0 .. settings.maxFailedLogins) {
            LoginAction(accountName, Secret("")).execute()
        }

        transaction {
            val accountBl = server.first<AccountPrivateBl>()
            assertTrue(accountBl.byName(accountName).locked)
        }
    }

    private suspend fun createAccount() : Triple<String, Secret, AccountPrivateBo> {
        val accountName = UUID().toString()
        val password = Secret(UUID().toString())

        val create = CreateAccount(
            credentials = password,
            accountName = accountName,
            fullName = "Test Account",
            email = "a@b.c",
            phone = null,
            displayName = null,
            theme = null,
            locale = "en",
            roles = emptyList()
        )

        assertTrue(create.execute().success)

        val bo = transaction {
            val accountBl = server.first<AccountPrivateBl>()
            accountBl.byName(accountName)
        }

        return Triple(accountName, password, bo)
    }
}