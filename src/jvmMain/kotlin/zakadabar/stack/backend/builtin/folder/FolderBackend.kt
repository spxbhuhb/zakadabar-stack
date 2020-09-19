/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.folder

import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.entities.data.EntityDao
import zakadabar.stack.backend.comm.http.DtoBackend
import zakadabar.stack.data.builtin.FolderDto
import zakadabar.stack.util.Executor
import zakadabar.stack.util.PublicApi

@PublicApi
object FolderBackend : DtoBackend<FolderDto>() {

    override val dtoClass = FolderDto::class

    override fun install(route: Route) = route.crud()

    override fun create(executor: Executor, dto: FolderDto) = transaction {

        val entityDto = dto.entityRecord?.requireType(FolderDto.type) ?: throw IllegalArgumentException()

        val dao = EntityDao.create(executor, entityDto)

        FolderDto(
            id = dao.id.value,
            entityRecord = dao.toDto()
        )

    }

    override fun update(executor: Executor, dto: FolderDto) = transaction {

        val entityDto = dto.entityRecord ?: throw IllegalArgumentException()

        val entityDao = EntityDao.update(executor, entityDto)

        FolderDto(
            id = entityDao.id.value,
            entityRecord = entityDao.toDto()
        )
    }

}