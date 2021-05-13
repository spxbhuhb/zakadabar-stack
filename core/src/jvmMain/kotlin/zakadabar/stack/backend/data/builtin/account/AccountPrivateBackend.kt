/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.builtin.account.AccountPrivateBackend.onModuleLoad
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.builtin.resources.setting
import zakadabar.stack.backend.data.builtin.role.RoleDao
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantDao
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantTable
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.account.AccountPrivateDto
import zakadabar.stack.data.builtin.misc.ServerDescriptionDto
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

/**
 * An account backend for the demo. You probably want to change this class to suit your
 * needs.
 *
 * Be careful with this one, it is intertwined with sessions and principals. See
 * comments in [onModuleLoad].
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

    private val serverDescription by setting<ServerDescriptionDto>("zakadabar.server.description")

    override fun onModuleLoad() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccountPrivateTable
            )
        }
    }

    override fun onModuleStart() {

        // create the tables we need, actually this backend needs [PrincipalTable] also
        // but that should be created by the principal backend

        // FIXME You probably want to change this in production code!
        // Create the "demo" user. In production code this user is usually a master admin
        // or something like that to initialize the system setup.

        roles()
        demo()
        so()

        // set the anonymous user of the server, creates an anonymous user if missing

        Server.anonymous = anonymous()

        // provide account lookup by id for the server, this is used by the session backend
        // to get the account information and the roles

        Server.findAccountById = {
            transaction {
                val account = AccountPrivateDao[it.toLong()]
                account.toPublicDto(false) to LongRecordId(account.principal.id.value)
            }
        }

        // provide account lookup by name for the server, this is used by the session backend
        // to get the account information and the roles

        Server.findAccountByName = {
            transaction {
                val account = AccountPrivateDao.find { AccountPrivateTable.accountName eq it }.firstOrNull() ?: throw NoSuchElementException()
                account.toPublicDto(false) to LongRecordId(account.principal.id.value)
            }
        }
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        AccountPrivateTable
            .selectAll()
            .map(AccountPrivateTable::toDto)
    }

    override fun create(executor: Executor, dto: AccountPrivateDto) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        val newPrincipal = PrincipalDao.new {
            validated = true
        }

        AccountPrivateDao.new {
            principal = newPrincipal
            fromDto(dto)
        }.toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<AccountPrivateDto>) = transaction {

        // the owner of the account and security officers may read it

        if (recordId != executor.accountId) {
            authorize(executor, StackRoles.securityOfficer)
        }

        AccountPrivateDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: AccountPrivateDto) = transaction {

        // the owner of the account and security officers may read it

        if (dto.id != executor.accountId) {
            authorize(executor, StackRoles.securityOfficer)
        }

        AccountPrivateDao[dto.id].fromDto(dto).toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<AccountPrivateDto>) {

        authorize(executor, StackRoles.securityOfficer)

        AccountPrivateDao[recordId].delete()
    }

    private fun AccountPrivateDao.fromDto(dto: AccountPrivateDto): AccountPrivateDao {
        val avatarId = dto.avatar

        accountName = dto.accountName
        fullName = dto.fullName
        email = dto.email

        displayName = dto.displayName
        theme = dto.theme
        locale = dto.locale
        avatar = avatarId?.let { AccountImageDao[avatarId] }

        organizationName = dto.organizationName
        position = dto.position
        phone = dto.phone

        return this
    }

    private fun roles() = transaction {
        role(StackRoles.securityOfficer)
        role(StackRoles.siteMember)
    }

    private fun role(roleName: String) {
        if (RoleDao.find { RoleTable.name eq roleName }.firstOrNull() == null) {
            RoleDao.new {
                name = roleName
                description = roleName
            }.toDto()
        }
    }

    /**
     * Get the anonymous account or create one if doesn't exist. Called by [onModuleLoad],
     * creates principal and account for anonymous if none exists.
     */
    private fun anonymous() = transaction {
        val account = AccountPrivateTable
            .select { AccountPrivateTable.accountName eq "anonymous" }
            .map { AccountPrivateTable.toPublicDto(it) }
            .firstOrNull()

        if (account != null) return@transaction account

        val newPrincipal = PrincipalDao.new {
            validated = false
            locked = true
        }

        val newAccount = AccountPrivateDao.new {
            principal = newPrincipal
            accountName = "anonymous"
            fullName = "anonymous"
            displayName = "anonymous"
            locale = serverDescription.defaultLocale
            organizationName = ""
            position = ""
            email = ""
            phone = ""
        }

        newAccount.toPublicDto(false)
    }


    /**
     * Create a demo account if doesn't exist. Called by [onModuleLoad],
     * creates principal and account for demo if none exists.
     *
     * FIXME You probably want to remove or change this function in production code!
     */
    private fun demo() {
        val account = transaction {

            val existing = AccountPrivateDao
                .find { AccountPrivateTable.accountName eq "demo" }
                .firstOrNull()

            if (existing != null) return@transaction existing

            val newPrincipal = PrincipalDao.new {
                credentials = PrincipalBackend.encrypt("demo")
                validated = true
            }

            AccountPrivateDao.new {
                principal = newPrincipal
                accountName = "demo"
                fullName = "Demo"
                displayName = "Demo"
                locale = serverDescription.defaultLocale
                organizationName = ""
                position = ""
                email = ""
                phone = ""
            }
        }

        grant(account, StackRoles.siteMember)
    }

    private fun so() {
        val account = transaction {

            val existing = AccountPrivateDao
                .find { AccountPrivateTable.accountName eq "so" }
                .firstOrNull()

            if (existing != null) return@transaction existing

            val newPrincipal = PrincipalDao.new {
                credentials = PrincipalBackend.encrypt("rkDxoU6eNPoujMig")
                validated = true
            }

            AccountPrivateDao.new {
                principal = newPrincipal
                accountName = "so"
                fullName = "Security Officer"
                displayName = "Security Officer"
                locale = serverDescription.defaultLocale
                organizationName = ""
                position = ""
                email = ""
                phone = ""
            }
        }

        grant(account, StackRoles.securityOfficer)
        grant(account, StackRoles.siteMember)
    }

    private fun grant(account: AccountPrivateDao, roleName: String) = transaction {
        val role = RoleDao.find { RoleTable.name eq roleName }.first()
        val grant = RoleGrantDao.find { (RoleGrantTable.role eq role.id) and (RoleGrantTable.principal eq account.principal.id) }.firstOrNull()
        if (grant != null) return@transaction

        RoleGrantDao.new {
            this.role = role
            principal = account.principal
        }
    }
}