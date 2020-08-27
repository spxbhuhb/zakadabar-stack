/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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