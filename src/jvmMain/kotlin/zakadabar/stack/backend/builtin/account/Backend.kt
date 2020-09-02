/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.account

import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.account.data.AccountDao
import zakadabar.stack.backend.builtin.account.data.AccountTable
import zakadabar.stack.backend.builtin.account.data.AccountTable.toDto
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.extend.EntityRestBackend
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.util.BCrypt
import zakadabar.stack.util.Executor

object Backend : EntityRestBackend<CommonAccountDto> {

    override fun query(executor: Executor, id: Long?, parentId: Long?): List<CommonAccountDto> = transaction {

        val condition = if (id == null) {
            (EntityTable.parent eq parentId) and (EntityTable.type eq CommonAccountDto.type)
        } else {
            AccountTable.id eq id
        }

        AccountTable
            .join(EntityTable, JoinType.INNER) {
                EntityTable.id eq AccountTable.id
            }
            .select(condition)
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

        val edto = dto.entityRecord?.requireType(CommonAccountDto.type) ?: throw IllegalArgumentException()

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
}