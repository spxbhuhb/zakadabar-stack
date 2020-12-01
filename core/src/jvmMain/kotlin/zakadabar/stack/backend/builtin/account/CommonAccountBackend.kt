/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.account.data.AccountDao
import zakadabar.stack.backend.builtin.account.data.AccountTable
import zakadabar.stack.backend.builtin.account.data.AccountTable.toDto
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.data.RecordBackend
import zakadabar.stack.data.builtin.security.CommonAccountDto
import zakadabar.stack.data.builtin.security.SearchAccounts
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.util.BCrypt
import zakadabar.stack.util.Executor
import zakadabar.stack.util.PublicApi

@PublicApi
object CommonAccountBackend : RecordBackend<CommonAccountDto>() {

    override val dtoClass = CommonAccountDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccountTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
        route.query(SearchAccounts::class, ::search)
    }

    private fun search(executor: Executor, query: SearchAccounts) = transaction {
        AccountTable
            .join(EntityTable, JoinType.INNER) {
                EntityTable.id eq AccountTable.id
            }
            .select { AccountTable.id inList query.accountIds }
            .filter { EntityTable.readableBy(executor, it) }
            .map(::toDto)
    }

    override fun all(executor: Executor) = transaction {
        AccountTable
            .join(EntityTable, JoinType.INNER) {
                EntityTable.id eq AccountTable.id
            }
            .selectAll()
            .filter { EntityTable.readableBy(executor, it) }
            .map(::toDto)
    }

    override fun create(executor: Executor, dto: CommonAccountDto) = transaction {

        // ---- validate the account ---------

        // FIXME duplication check

        // ---- check the avatar ---------

        val dtoAvatar = dto.avatar
        val avatarDao = if (dtoAvatar == null) null else
            EntityDao[dtoAvatar]
                .requireControlFor(executor)
                .requireStatus(EntityStatus.Pending)
                .requireType("image/png", "image/jpeg")

        // ---- create the entity record ---------

        val edto = dto.entityRecord?.requireType(CommonAccountDto.recordType) ?: throw IllegalArgumentException()

        val edao = EntityDao.create(executor, edto) // performs authorization

        // ---- move the avatar under the entity ---------

        if (avatarDao != null) {
            avatarDao.status = EntityStatus.Active
            avatarDao.parent = edao
        }

        // ---- create the account record ---------

        AccountTable.insert {
            it[id] = edao.id
            it[emailAddress] = dto.emailAddress
            it[fullName] = dto.fullName
            it[displayName] = dto.displayName
            it[organizationName] = dto.organizationName
            it[avatar] = avatarDao?.id
            it[password] = BCrypt.hashpw(dto.password, BCrypt.gensalt(10)) // TODO get salt number from config
        }

        dto.copy(id = edao.id.value, entityRecord = edao.toDto())
    }


    override fun read(executor: Executor, recordId: Long) = transaction {

        EntityDao[recordId].requireReadFor(executor)
        val accountDao = AccountDao[recordId]

        accountDao.toDto()

    }

    override fun update(executor: Executor, dto: CommonAccountDto) = transaction {

        // validate the account

        // FIXME duplication check

        // check the avatar

        val dtoAvatar = dto.avatar
        val avatarDao = if (dtoAvatar == null) null else
            EntityDao[dtoAvatar]
                .requireControlFor(executor)
                .requireType("image/png", "image/jpeg")
                .requireStatus(EntityStatus.Pending, EntityStatus.Active)

        // update the entity record

        val entityDto = dto.entityRecord?.requireId(dto.id) ?: throw IllegalArgumentException()

        val entityDao = EntityDao.update(executor, entityDto) // performs authorization

        // update the account record

        val accountDao = AccountDao[dto.id]

        accountDao.emailAddress = dto.emailAddress
        accountDao.fullName = dto.fullName
        accountDao.displayName = dto.displayName
        accountDao.organizationName = dto.organizationName

        // update the avatar if changed

        if (avatarDao?.id != accountDao.avatar) {

            val oldAvatar = accountDao.avatar
            if (oldAvatar != null) {
                EntityDao[oldAvatar].markForDelete(executor)
            }

            if (avatarDao != null) {
                avatarDao.status = EntityStatus.Active
                avatarDao.parent = entityDao
            }

            accountDao.avatar = avatarDao?.id
        }

        accountDao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        throw NotImplementedError()
    }
}