/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend.bl

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.lib.accounts.backend.*
import zakadabar.lib.accounts.backend.testing.AuthTestCompanionBase
import zakadabar.lib.accounts.data.*
import zakadabar.core.server.server
import zakadabar.core.setting.setting
import zakadabar.core.data.builtin.misc.Secret
import zakadabar.core.data.entity.EntityId
import zakadabar.core.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


class AccountBlTest {

    companion object : AuthTestCompanionBase(
        roles = Roles,
        credentials = null // logins are handled manually in these tests
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

        assertUnauthorizedForAnonymous {
            createAccount(useSo = false)
        }

        assertForbiddenForNew {
            createAccount(useSo = false)
        }

        val (accountName, password, bo) = createAccount()

        assertEquals(accountName, bo.accountName)

        assertTrue(LoginAction(accountName, password).execute().success)
        assertTrue(LogoutAction().execute().success)
    }

    @Test
    fun testUpdateLockedAccount() = runBlocking {

        val (accountName, password, bo) = createAccount()
        assertTrue(LoginAction(accountName, password).execute().success)

        assertUnauthorizedForAnonymous {
            UpdateAccountLocked(bo.id, true).execute()
        }

        assertForbiddenForNew {
            UpdateAccountLocked(bo.id, true).execute()
        }

        withSo {
            UpdateAccountLocked(bo.id, true).execute()
        }

        assertUnauthorized {
            LoginAction(accountName, password).execute()
        }

        withSo {
            UpdateAccountLocked(bo.id, false).execute()
        }

        assertTrue(LoginAction(accountName, password).execute().success)
    }

    @Test
    fun testAutoLock() = runBlocking {

        val (accountName, _, account) = createAccount()

        val func = suspend {
            LoginAction(accountName, Secret("")).execute()
        }

        assertTriesLock(account.id, func)
    }

    private suspend fun assertTriesLock(accountId: EntityId<AccountPrivateBo>, func: suspend () -> Any) {
        val settings by setting<ModuleSettings>()

        for (i in 0..settings.maxFailedLogins) {
            assertUnauthorized {
                func()
            }
        }

        transaction {
            val accountBl = server.first<AccountPrivateBl>()
            assertTrue(accountBl.statePa.read(accountId).locked)
        }
    }

    @Test
    fun testAuthFail() = runBlocking {

        val (accountName, _, _) = createAccount()

        assertUnauthorized {
            LoginAction(UUID().toString(), Secret("")).execute()
        }

        assertUnauthorized {
            LoginAction(accountName, Secret(UUID.toString())).execute()
        }

        Unit
    }

    @Test
    fun testCheckName() = runBlocking {
        val (accountName, _, account) = createAccount()

        withAnonymous {
            var result = CheckName(accountName).execute()
            assertEquals(account.id.toLong(), result.accountId?.toLong())

            result = CheckName(UUID().toString()).execute()
            assertNull(result.accountId)
        }
    }

    @Test
    fun testAccountState() = runBlocking {

        val account = createAccount().third

        assertUnauthorizedForAnonymous {
            GetAccountState(account.id).execute()
        }

        assertForbiddenForNew {
            GetAccountState(account.id).execute()
        }

        withSo {
            GetAccountState(account.id).execute()
        }

        Unit
    }

    @Test
    fun testPasswordChange() = runBlocking {

        val (accountName, password, account) = createAccount()

        // don't allow from anonymous

        assertUnauthorizedForAnonymous {
            withAnonymous {
                PasswordChange(account.id, password, Secret("new password")).execute()
            }
        }

        // don't allow with wrong password, executed by self

        assertUnauthorized {
            withLogin(accountName, password) {
                PasswordChange(account.id, Secret("wrong"), Secret("new password")).execute()
            }
        }

        // allow with right password, executed by self

        withLogin(accountName, password) {
            PasswordChange(account.id, password, Secret("new password")).execute()
        }

        // allow with any passwords, executed by so

        val bySo = Secret("password set by so")

        withSo {
            PasswordChange(account.id, Secret("wrong"), bySo).execute()
        }

        LoginAction(accountName, bySo).execute() // test the change by the SO

        // lock account with max tries

        val func = suspend {
            PasswordChange(account.id, Secret("wrong"), password).execute()
        }

        assertTriesLock(account.id, func)

        // SO should not be able to set own password without providing the old

        assertUnauthorized {
            withSo {
                val accountBl = server.first<AccountPrivateBl>()
                val so = transaction {
                    accountBl.byName("so")
                }
                PasswordChange(so.id, Secret("wrong"), Secret("new passwoed")).execute()
            }
        }

        Unit
    }

    @Test
    fun testAccountList() = runBlocking {

        val settings = server.first<AccountPrivateBl>().settings

        assertUnauthorizedForAnonymous {
            AccountList().execute()
        }

        val account = loginWithNew()

        settings.enableAccountList = false

        assertForbidden {
            AccountList().execute()
        }

        settings.enableAccountList = true
        settings.emailInAccountPublic = true
        settings.phoneInAccountPublic = true

        var accounts = AccountList().execute()

        assertTrue(accounts.isNotEmpty())
        accounts.first { account.id == it.accountId }
            .also {
                assertEquals(account.accountName, it.accountName)
                assertEquals(account.fullName, it.fullName)
                assertEquals(account.email, it.email)
                assertEquals(account.phone, it.phone)
                assertEquals(account.theme, it.theme)
                assertEquals(account.locale, it.locale)
            }

        settings.emailInAccountPublic = false

        accounts = AccountList().execute()

        assertTrue(accounts.isNotEmpty())
        accounts.forEach {
            assertTrue(it.email.isNullOrEmpty())
        }

        settings.phoneInAccountPublic = false

        accounts = AccountList().execute()

        assertTrue(accounts.isNotEmpty())
        accounts.forEach {
            assertTrue(it.phone.isNullOrEmpty())
        }
    }

}