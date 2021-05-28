/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantTable
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.get
import zakadabar.stack.data.builtin.account.AccountByRole
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.entity.EntityId

object AccountPublicBackend : EntityBackend<AccountPublicBo>() {

    override val boClass = AccountPublicBo::class

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.query(AccountByRole::class, AccountPublicBackend::query)
    }

    private fun query(executor: Executor, query: AccountByRole) = transaction {
        RoleTable
            .join(RoleGrantTable, JoinType.INNER, additionalConstraint = { RoleGrantTable.role eq RoleTable.id })
            .join(AccountPrivateTable, JoinType.INNER, additionalConstraint = { RoleGrantTable.principal eq AccountPrivateTable.principal })
            .slice(
                AccountPrivateTable.id,
                AccountPrivateTable.accountName,
                AccountPrivateTable.fullName,
                AccountPrivateTable.displayName,
                AccountPrivateTable.organizationName,
                AccountPrivateTable.email
            )
            .select { RoleTable.name eq query.roleName }
            .map(AccountPrivateTable::toPublicBo)
    }

    override fun all(executor: Executor) = transaction {
        AccountPrivateTable
            .selectAll()
            .map(AccountPrivateTable::toPublicBo)
    }

    override fun read(executor: Executor, entityId: EntityId<AccountPublicBo>) = transaction {
        AccountPrivateDao[entityId].toPublicBo(false)
    }

}