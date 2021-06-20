/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.stack.RolesBase
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.persistence.EmptyPersistenceApi
import zakadabar.stack.backend.server
import zakadabar.stack.data.entity.EmptyEntityBo
import zakadabar.stack.data.entity.EntityId
import kotlin.test.assertFailsWith

internal object WithDefault : EntityBusinessLogicBase<EmptyEntityBo>(EmptyEntityBo::class) {
    override val pa = EmptyPersistenceApi<EmptyEntityBo>()
    override val namespace = "not-used"
    override val authorizer by provider()
}

internal object WithQuery : EntityBusinessLogicBase<EmptyEntityBo>(EmptyEntityBo::class) {
    override val pa = EmptyPersistenceApi<EmptyEntityBo>()
    override val namespace = "not-used"
    override val authorizer by provider {
        this as SimpleRoleAuthorizer
        query(Query::class, StackRoles.siteMember)
    }
}

class SimpleRoleAuthorizerProviderTest {

    companion object : TestCompanion() {

        @BeforeClass
        @JvmStatic
        fun setup() = super.setup {

            StackRoles = RolesBase()

            server += MockRoleBlProvider

            server += SimpleRoleAuthorizerProvider {
                all = StackRoles.siteMember
            }

            server += WithDefault
            server += WithQuery
        }

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testProvider() = runBlocking {

        val anonymousRoleId = server.first<RoleBlProvider>().getByName(StackRoles.anonymous)
        val siteMemberRoleId = server.first<RoleBlProvider>().getByName(StackRoles.siteMember)

        val siteMember = Executor(EntityId(1), false, listOf(siteMemberRoleId), listOf(StackRoles.siteMember))
        val anonymous = Executor(EntityId(2), true, listOf(anonymousRoleId), listOf(StackRoles.anonymous))

        WithDefault.authorizer.authorizeRead(siteMember, EntityId(1))

        assertFailsWith(Forbidden::class) {
            WithDefault.authorizer.authorizeRead(anonymous, EntityId(1))
        }

        WithQuery.authorizer.authorizeQuery(siteMember, Query())

        assertFailsWith(Forbidden::class) {
            WithQuery.authorizer.authorizeQuery(anonymous, Query())
        }

        Unit
    }

}