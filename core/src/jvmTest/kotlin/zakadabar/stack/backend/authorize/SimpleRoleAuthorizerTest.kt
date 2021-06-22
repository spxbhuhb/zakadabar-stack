/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer.Companion.PUBLIC
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.persistence.EmptyPersistenceApi
import zakadabar.stack.backend.server
import zakadabar.stack.backend.testing.TestCompanionBase
import zakadabar.stack.data.entity.EmptyEntityBo
import zakadabar.stack.data.entity.EntityId
import kotlin.test.assertFailsWith

internal object WithPublic : EntityBusinessLogicBase<EmptyEntityBo>(EmptyEntityBo::class) {
    override val pa = EmptyPersistenceApi<EmptyEntityBo>()
    override val namespace = "not-used"
    override val authorizer = SimpleRoleAuthorizer<EmptyEntityBo> {
        all = PUBLIC
        action(Action::class, PUBLIC)
        query(Query::class, PUBLIC)
    }
}

class SimpleRoleAuthorizerTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            server += WithPublic
        }

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testPublic() = runBlocking {

        val withPublic = server.first<WithPublic>()

        val anonymousRoleId = server.first<RoleBlProvider>().getByName(StackRoles.anonymous)
        val siteMemberRoleId = server.first<RoleBlProvider>().getByName(StackRoles.siteMember)

        val siteMember = Executor(EntityId(1), false, listOf(siteMemberRoleId), listOf(StackRoles.siteMember))
        val anonymous = Executor(EntityId(2), true, listOf(anonymousRoleId), listOf(StackRoles.anonymous))

        with(withPublic.authorizer) {
            authorizeList(anonymous)
            authorizeList(siteMember)
            authorizeRead(anonymous, EntityId(1))
            authorizeRead(siteMember, EntityId(1))
            authorizeCreate(anonymous, EmptyEntityBo)
            authorizeCreate(siteMember, EmptyEntityBo)
            authorizeUpdate(anonymous, EmptyEntityBo)
            authorizeUpdate(siteMember, EmptyEntityBo)
            authorizeDelete(anonymous, EntityId(1))
            authorizeDelete(siteMember, EntityId(1))
            authorizeAction(anonymous, Action())
            authorizeAction(siteMember, Action())
            authorizeQuery(anonymous, Query())
            authorizeQuery(siteMember, Query())
        }

        with(SimpleRoleAuthorizer<EmptyEntityBo>()) {
            assertFailsWith<Forbidden> { authorizeList(anonymous) }
            assertFailsWith<Forbidden> { authorizeList(siteMember) }
            assertFailsWith<Forbidden> { authorizeRead(anonymous, EntityId(1)) }
            assertFailsWith<Forbidden> { authorizeRead(siteMember, EntityId(1)) }
            assertFailsWith<Forbidden> { authorizeCreate(anonymous, EmptyEntityBo) }
            assertFailsWith<Forbidden> { authorizeCreate(siteMember, EmptyEntityBo) }
            assertFailsWith<Forbidden> { authorizeUpdate(anonymous, EmptyEntityBo) }
            assertFailsWith<Forbidden> { authorizeUpdate(siteMember, EmptyEntityBo) }
            assertFailsWith<Forbidden> { authorizeDelete(anonymous, EntityId(1)) }
            assertFailsWith<Forbidden> { authorizeDelete(siteMember, EntityId(1)) }
            assertFailsWith<Forbidden> { authorizeAction(anonymous, Action()) }
            assertFailsWith<Forbidden> { authorizeAction(siteMember, Action()) }
            assertFailsWith<Forbidden> { authorizeQuery(anonymous, Query()) }
            assertFailsWith<Forbidden> { authorizeQuery(siteMember, Query()) }
        }

        Unit
    }

}