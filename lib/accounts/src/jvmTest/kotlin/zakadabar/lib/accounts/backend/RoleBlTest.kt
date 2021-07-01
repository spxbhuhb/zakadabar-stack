/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.lib.accounts.data.AccountsByRole
import zakadabar.lib.accounts.data.CreateAccount
import zakadabar.lib.accounts.data.RoleBo
import kotlin.test.assertEquals

class RoleBlTest {

    companion object : TestCompanion() {

        @BeforeClass
        @JvmStatic
        fun setup() = super.setup("so", "so")

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testAccounsByRole() = runBlocking {
        var siteMember = AccountsByRole(Roles.siteMember).execute()
        assertEquals(1, siteMember.size)
        assertEquals("so", siteMember.first().accountName)

        CreateAccount(
            credentials = null,
            accountName = "test1",
            fullName = "test1",
            email = "email@a.b",
            phone = "12345678901234",
            displayName = null,
            theme = null,
            locale = "en",
            roles = listOf(RoleBo.all().first { it.name == Roles.siteMember }.id)
        ).execute()

        siteMember = AccountsByRole(Roles.siteMember).execute().sortedBy { it.accountName }
        assertEquals(2, siteMember.size)
        assertEquals("so", siteMember.first().accountName)
        assertEquals("test1", siteMember[1].accountName)
    }

}