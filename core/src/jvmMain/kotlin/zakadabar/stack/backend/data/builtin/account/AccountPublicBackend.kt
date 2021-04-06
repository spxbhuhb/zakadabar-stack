/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantTable
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.account.AccountByRole
import zakadabar.stack.data.builtin.account.AccountPublicDto
import zakadabar.stack.util.Executor

object AccountPublicBackend : RecordBackend<AccountPublicDto>() {

    override val dtoClass = AccountPublicDto::class

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
            .map(AccountPrivateTable::toPublicDto)
    }

    override fun all(executor: Executor) = transaction {
        AccountPrivateTable
            .selectAll()
            .map(AccountPrivateTable::toPublicDto)
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        AccountPrivateDao[recordId].toPublicDto(false)
    }

}