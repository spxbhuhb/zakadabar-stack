/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.EmptyEntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.exception.Forbidden
import zakadabar.core.persistence.EmptyPersistenceApi
import zakadabar.core.server.server
import zakadabar.core.testing.TestCompanionBase
import zakadabar.core.util.UUID
import kotlin.test.assertFailsWith

object Roles : AppRolesBase() {
    val testRole by "test-role"
}

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
        query(TestQuery::class, LOGGED_IN)
        action(TestAction::class, Roles.testRole)
    }
}

class SimpleRoleAuthorizerProviderTest {

    companion object : TestCompanionBase(
        allPublic = false,
        roles = Roles
    ) {

        override fun addModules() {
            server += SimpleRoleAuthorizerProvider {
                allReads = LOGGED_IN
                allWrites = Roles.testRole
            }

            server += WithDefault
            server += WithQuery
        }

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testProvider() = runBlocking {

        val testRoleId = server.first<RoleBlProvider>().getByName(Roles.testRole)

        val siteMember = Executor(EntityId(1), UUID.NIL, false, listOf(testRoleId), listOf(Roles.testRole))
        val anonymous = Executor(EntityId(2), UUID.NIL, true, emptyList(), emptyList())

        WithDefault.authorizer.authorizeRead(siteMember, EntityId(1))
        WithDefault.authorizer.authorizeUpdate(siteMember, EmptyEntityBo)

        assertFailsWith(Forbidden::class) {
            WithDefault.authorizer.authorizeRead(anonymous, EntityId(1))
        }

        assertFailsWith(Forbidden::class) {
            WithDefault.authorizer.authorizeUpdate(anonymous, EmptyEntityBo)
        }

        WithQuery.authorizer.authorizeQuery(siteMember, TestQuery())
        WithQuery.authorizer.authorizeAction(siteMember, TestAction())

        assertFailsWith(Forbidden::class) {
            WithQuery.authorizer.authorizeQuery(anonymous, TestQuery())
        }

        assertFailsWith(Forbidden::class) {
            WithQuery.authorizer.authorizeAction(anonymous, TestAction())
        }

        Unit
    }

}