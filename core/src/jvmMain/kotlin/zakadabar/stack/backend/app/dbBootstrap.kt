/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.app

import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.account.CommonAccountBackend
import zakadabar.stack.backend.builtin.account.data.AccountTable
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.builtin.entities.data.Locks
import zakadabar.stack.backend.builtin.entities.data.SnapshotTable
import zakadabar.stack.backend.builtin.session.data.SessionTable
import zakadabar.stack.backend.util.Sql
import zakadabar.stack.data.builtin.FolderDto
import zakadabar.stack.data.builtin.SystemDto
import zakadabar.stack.data.builtin.security.CommonAccountDto
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.util.Executor

fun dbBootstrap(config: Configuration) {

    Sql.init(config.database)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            EntityTable,
            SnapshotTable,
            Locks,
            SessionTable,
            AccountTable // FIXME make this swappable
        )
    }

    transaction {
        val systemDao = EntityDao.find { EntityTable.type eq SystemDto.type }.firstOrNull()

        if (systemDao != null) return@transaction

        val now = Clock.System.now().toJavaInstant()

        // this is a temporary entity for bootstrap

        EntityTable.insert {
            it[id] = EntityID(0L, EntityTable)
            it[acl] = EntityID(0L, EntityTable)
            it[status] = EntityStatus.Active
            it[parent] = null
            it[name] = "bootstrap"
            it[type] = "bootstrap"
            it[size] = 0
            it[revision] = 1
            it[createdAt] = now
            it[createdBy] = EntityID(0L, EntityTable)
            it[modifiedAt] = now
            it[modifiedBy] = EntityID(0L, EntityTable)
        }

        val system = EntityDao.new {
            name = "System"
            acl = null
            status = EntityStatus.Active
            parent = null
            type = SystemDto.type
            size = 0
            revision = 1
            createdAt = now
            createdBy = EntityDao[0L]
            modifiedAt = now
            modifiedBy = EntityDao[0L]
        }

        commit()

        system.createdBy = system
        system.modifiedBy = system

        commit()

        EntityTable.deleteWhere { EntityTable.id eq 0L }

        val pid = Executor(system.id.value)

        val accountsDao = EntityDao.create("accounts", FolderDto.type, 1L, EntityStatus.Active, pid)
        EntityDao.create("roles", FolderDto.type, 1L, EntityStatus.Active, pid)
        EntityDao.create("tmp", FolderDto.type, 1L, EntityStatus.Active, pid)
        EntityDao.create("bin", FolderDto.type, 1L, EntityStatus.Active, pid)

        val soDto = CommonAccountDto(
            id = 0,
            entityRecord = EntityRecordDto.new(accountsDao.id.value, CommonAccountDto.type, "so"),
            emailAddress = "noreply@simplexion.hu",
            fullName = "Security Officer",
            displayName = "Security Officer",
            organizationName = "Simplexion Kft.",
            avatar = null,
            password = "df;kgh32yfsdhfoia"
        )

        CommonAccountBackend.create(pid, soDto)

        val anonymousDto = CommonAccountDto(
            id = 0,
            entityRecord = EntityRecordDto.new(accountsDao.id.value, CommonAccountDto.type, "anonymous"),
            emailAddress = "noreply@simplexion.hu",
            fullName = "Anonymous",
            displayName = "Anonymous",
            organizationName = "Simplexion Kft.",
            avatar = null,
            password = "df;kgh32yfsdhfoia"
        )

        CommonAccountBackend.create(pid, anonymousDto)

    }

}