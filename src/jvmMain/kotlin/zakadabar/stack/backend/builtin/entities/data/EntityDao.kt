/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities.data

import io.ktor.features.*
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.BackendContext
import zakadabar.stack.backend.util.AuthorizationException
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.data.entity.EntityStatus
import zakadabar.stack.util.Executor
import zakadabar.stack.util.PublicApi
import java.time.LocalDateTime
import java.time.ZoneOffset

class EntityDao(id: EntityID<Long>) : LongEntity(id) {

    var acl by EntityDao optionalReferencedOn EntityTable.acl
    var status by EntityTable.status
    var parent by EntityDao optionalReferencedOn EntityTable.parent
    var revision by EntityTable.revision
    var type by EntityTable.type
    var name by EntityTable.name
    var size by EntityTable.size
    var createdAt by EntityTable.createdAt
    var createdBy by EntityDao referencedOn EntityTable.createdBy
    var modifiedAt by EntityTable.modifiedAt
    var modifiedBy by EntityDao referencedOn EntityTable.modifiedBy

    fun toDto() = EntityDto(
        id = id.value,
        acl = acl?.id?.value,
        status = status,
        parentId = parent?.id?.value,
        revision = revision,
        type = type,
        name = name,
        size = size,
        modifiedAt = modifiedAt.toEpochSecond(ZoneOffset.UTC),
        modifiedBy = modifiedBy.id.value
    )

    /**
     * Checks if the given principal has read access according to the given
     * security domain.
     */
    fun requireReadFor(executor: Executor): EntityDao {
        // FIXME proper security check
        return this
    }

    /**
     * Checks if the given principal has write access according to the given
     * security domain.
     */
    fun requireWriteFor(executor: Executor): EntityDao {
        // FIXME proper security check
        return this
    }

    /**
     * Checks if the given principal has control access according to the given
     * security domain.
     */
    fun requireControlFor(executor: Executor): EntityDao {
        // FIXME proper security check
        return this
    }

    fun requireType(type: String): EntityDao {
        require(this.type == type)
        return this
    }

    fun requireType(vararg types: String): EntityDao {
        require(this.type in types)
        return this
    }

    fun requireStatus(status: EntityStatus): EntityDao {
        require(this.status == status)
        return this
    }

    fun requireStatus(vararg statuses: EntityStatus): EntityDao {
        require(this.status in statuses)
        return this
    }

    fun markForDelete(executor: Executor) {

        BackendContext.requireWriteFor(executor, id.value)

        status = EntityStatus.MarkedForDelete
        modifiedBy = EntityDao[executor.entityId]
        modifiedAt = LocalDateTime.now()

    }

    companion object : LongEntityClass<EntityDao>(EntityTable) {

        /**
         * Create a new entity, with revision 1 and size 0.
         *
         * Performs authorization check on the parent entity.
         *
         * Must be called from within a [transaction], does not perform commit.
         *
         * @return  the created entity
         *
         * @throws  NotFoundException        when the parent entity cannot be found
         * @throws  AuthorizationException   when the principal has no write access to the parent entity
         */
        @PublicApi
        fun create(
            name: String,
            mimeType: String,
            parentId: Long?,
            status: EntityStatus,
            executor: Executor
        ): EntityDao {

            val now = LocalDateTime.now()

            val domainId = BackendContext.requireWriteFor(executor, parentId)

            return EntityDao.new {
                this.name = name
                acl = EntityDao[domainId]
                this.status = status
                this.parent = if (parentId == null) null else EntityDao[parentId]
                type = mimeType
                size = 0
                revision = 1
                createdAt = now
                createdBy = EntityDao[executor.entityId]
                modifiedAt = now
                modifiedBy = EntityDao[executor.entityId]
            }
        }

        /**
         * Create a new entity.
         *
         * Performs authorization check on the parent entity.
         *
         * Must be called from within a [transaction], does not perform commit.
         *
         * @return  the created entity
         *
         * @throws  NotFoundException        when the parent entity cannot be found
         * @throws  AuthorizationException   when the principal has no write access to the parent entity
         */
        fun create(executor: Executor, dto: EntityDto): EntityDao {

            val now = LocalDateTime.now()

            BackendContext.requireWriteFor(executor, dto.parentId)

            val dtoAcl = dto.acl
            val dtoParentId = dto.parentId

            return EntityDao.new {
                name = dto.name
                acl = if (dtoAcl == null) null else EntityDao[dtoAcl]
                status = dto.status
                parent = if (dtoParentId == null) null else EntityDao[dtoParentId]
                type = dto.type
                size = dto.size
                revision = dto.revision
                createdAt = now
                createdBy = EntityDao[executor.entityId]
                modifiedAt = now
                modifiedBy = EntityDao[executor.entityId]
            }
        }


        fun update(executor: Executor, dto: EntityDto): EntityDao {

            val dao = EntityDao[dto.id].requireWriteFor(executor)

            val dtoParentId = dto.parentId

            dao.name = dto.name
            dao.status = dto.status
            dao.modifiedBy = EntityDao[executor.entityId]
            dao.modifiedAt = LocalDateTime.now()

            if (dao.parent?.id?.value != dto.parentId) {

                dao.requireControlFor(executor)

                dao.parent = if (dtoParentId != null) {

                    EntityDao[dtoParentId].requireWriteFor(executor)

                } else {

                    BackendContext.requireWriteFor(executor, null)
                    null

                }
            }

            return dao
        }

    }
}