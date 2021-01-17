/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.account.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.backend.account.account.AccountPrivateBackend.init
import zakadabar.demo.data.account.AccountPrivateDto
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.util.Executor

/**
 * An account backend for the demo. You probably want to change this class to suit your
 * needs.
 *
 * Be careful with this one, it is intertwined with sessions and principals. See
 * comments in [init].
 *
 * This class stores all the information that belongs to the account and it is private:
 * only the owner of the account and the administrators may access it.
 *
 * Public account information provided for other users uses [AccountPublicBackend].
 *
 * Accounts are linked to principals by [AccountPrivateTable.principal]. All authentication
 * and authorization functions should use the principal and the account should store only
 * the business data.
 */
object AccountPrivateBackend : RecordBackend<AccountPrivateDto>() {

    override val dtoClass = AccountPrivateDto::class

    var maxFailCount = 5

    override fun init() {

        // create the tables we need, actually this backend needs [PrincipalTable] also
        // but that should be created by the principal backend

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccountPrivateTable
            )
        }

        // set the anonymous user of the server, creates an anonymous user if missing

        Server.anonymous = anonymous()

        // provide account lookup by id for the server, this is used by the session backend
        // to get the account information and the roles

        Server.findAccountById = {
            transaction {
                val account = AccountPrivateDao[it]
                account.toPublicDto() to account.principal.id.value
            }
        }

        // provide account lookup by name for the server, this is used by the session backend
        // to get the account information and the roles

        Server.findAccountByName = {
            transaction {
                val account = AccountPrivateDao.find { AccountPrivateTable.accountName eq it }.firstOrNull() ?: throw NoSuchElementException()
                account.toPublicDto() to account.principal.id.value
            }
        }
    }

    override fun install(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        AccountPrivateTable
            .selectAll()
            .map(AccountPrivateTable::toDto)
    }

    override fun create(executor: Executor, dto: AccountPrivateDto) = transaction {

        val newPrincipal = PrincipalDao.new { }

        AccountPrivateDao.new {
            principal = newPrincipal
            fromDto(dto)
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        AccountPrivateDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: AccountPrivateDto) = transaction {
        AccountPrivateDao[dto.id].fromDto(dto).toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        AccountPrivateDao[recordId].delete()
    }

    private fun AccountPrivateDao.fromDto(dto: AccountPrivateDto): AccountPrivateDao {
        val avatarId = dto.avatar

        accountName = dto.accountName
        fullName = dto.fullName
        displayName = dto.displayName
        avatar = avatarId?.let { AccountImageDao[avatarId] }

        organizationName = dto.organizationName
        position = dto.position

        email = dto.email
        phone = dto.phone

        return this
    }


    /**
     * Get the anonymous account or create one if not exists. Called by [init],
     * creates principal and account for anonymous if none exists.
     */
    private fun anonymous() = transaction {
        val account = AccountPrivateTable
            .select { AccountPrivateTable.accountName eq "anonymous" }
            .map { AccountPrivateTable.toPublicDto(it) }
            .firstOrNull()

        if (account != null) return@transaction account

        val newPrincipal = PrincipalDao.new { }

        val newAccount = AccountPrivateDao.new {
            principal = newPrincipal
            accountName = "anonymous"
            fullName = "anonymous"
            displayName = "anonymous"
            organizationName = ""
            position = ""
            email = ""
            phone = ""
        }

        newAccount.toPublicDto()
    }

}