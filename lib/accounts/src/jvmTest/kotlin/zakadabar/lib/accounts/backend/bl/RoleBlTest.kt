/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend.bl

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.lib.accounts.backend.Roles
import zakadabar.lib.accounts.backend.testing.AuthTestCompanionBase
import zakadabar.lib.accounts.data.AccountsByRole
import zakadabar.lib.accounts.data.CreateAccount
import zakadabar.lib.accounts.data.RoleBo
import kotlin.test.assertEquals

class RoleBlTest {

    companion object : AuthTestCompanionBase() {

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testAccountsByRole() = runBlocking {
        var siteMember = AccountsByRole(Roles.securityOfficer).execute()
        assertEquals(1, siteMember.size)
        assertEquals("so", siteMember.first().accountName)

        CreateAccount(
            locked = false,
            validated = true,
            credentials = null,
            accountName = "test1",
            fullName = "test1",
            email = "email@a.b",
            phone = "12345678901234",
            theme = null,
            locale = "en",
            roles = listOf(RoleBo.all().first { it.name == Roles.securityOfficer }.id)
        ).execute()

        siteMember = AccountsByRole(Roles.securityOfficer).execute().sortedBy { it.accountName }
        assertEquals(2, siteMember.size)
        assertEquals("so", siteMember.first().accountName)
        assertEquals("test1", siteMember[1].accountName)
    }

}