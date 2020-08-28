/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.folder

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.extend.EntityRestBackend
import zakadabar.stack.data.FolderDto
import zakadabar.stack.util.Executor

object Backend : EntityRestBackend<FolderDto> {

    override fun query(executor: Executor, id: Long?, parentId: Long?): List<FolderDto> =
        transaction {

            val condition = if (id == null) {
                (EntityTable.parent eq parentId) and (EntityTable.type eq FolderDto.type)
            } else {
                (EntityTable.id eq id) and (EntityTable.type eq FolderDto.type)
            }

            EntityTable
                .select(condition)
                .filter {
                    EntityTable.readableBy(executor, it)
                }
                .map {
                    FolderDto(
                        id = it[EntityTable.id].value,
                        entityDto = EntityTable.toDto(it)
                    )
                }
        }

    override fun create(executor: Executor, dto: FolderDto) = transaction {

        val entityDto = dto.entityDto?.requireType(FolderDto.type) ?: throw IllegalArgumentException()

        val dao = EntityDao.create(executor, entityDto)

        FolderDto(
            id = dao.id.value,
            entityDto = dao.toDto()
        )

    }

    override fun update(executor: Executor, dto: FolderDto) = transaction {

        val entityDto = dto.entityDto ?: throw IllegalArgumentException()

        val entityDao = EntityDao.update(executor, entityDto)

        FolderDto(
            id = entityDao.id.value,
            entityDto = entityDao.toDto()
        )
    }
}