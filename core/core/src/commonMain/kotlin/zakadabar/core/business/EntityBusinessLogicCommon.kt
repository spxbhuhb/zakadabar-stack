/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.business

import zakadabar.core.authorize.Executor
import zakadabar.core.persistence.EntityPersistenceApi
import zakadabar.core.data.BaseBo
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.entity.EntityId
import kotlin.reflect.KClass

/**
 * Base class for entity backends. Supports CRUD, actions and queries.
 */
abstract class EntityBusinessLogicCommon<T : EntityBo<T>>  (
    /**
     * The class of BO this entity backend servers. Namespace is automatically
     * set to the namespace defined for this BO class.
     */
    val boClass: KClass<T>
) : BusinessLogicCommon<T>() {

    /**
     * The Persistence API this entity business logic uses.
     */
    abstract val pa: EntityPersistenceApi<T>

    override fun onModuleLoad() {
        pa.onModuleLoad()
    }

    open fun listWrapper(executor: Executor): List<T> =

        pa.withTransaction {

            authorizer.authorizeList(executor)

            val response = list(executor)

            auditor.auditList(executor)

            response

        }

    open fun readWrapper(executor: Executor, entityId: EntityId<T>): T =

        pa.withTransaction {

            authorizer.authorizeRead(executor, entityId)

            val response = read(executor, entityId)

            auditor.auditRead(executor, entityId)

            response

        }

    open fun createWrapper(executor: Executor, bo: T): T =

        pa.withTransaction {

            validator.validateCreate(executor, bo)

            authorizer.authorizeCreate(executor, bo)

            val response = create(executor, bo)

            auditor.auditCreate(executor, response)

            response

        }

    open fun updateWrapper(executor: Executor, bo: T): T =

        pa.withTransaction {

            validator.validateUpdate(executor, bo)

            authorizer.authorizeUpdate(executor, bo)

            val response = update(executor, bo)

            auditor.auditUpdate(executor, response)

            response
        }

    open fun deleteWrapper(executor: Executor, entityId: EntityId<T>) =

        pa.withTransaction {

            authorizer.authorizeDelete(executor, entityId)

            delete(executor, entityId)

            auditor.auditDelete(executor, entityId)

        }

    /**
     * Create a new entity.
     *
     * URL: `POST /api/<namespace>/entity`
     *
     * @param executor Executor of the operation.
     * @param bo The entity to create.
     *
     * @return BO of the entity created
     */
    open fun create(executor: Executor, bo: T): T {
        return pa.create(bo)
    }

    /**
     * Read an entity.
     *
     * URL: `GET /api/<namespace>/entity/<entityId>`
     *
     * @param executor Executor of the operation.
     * @param entityId The id of the entity to read.
     *
     * @return BO of the entity
     */
    open fun read(executor: Executor, entityId: EntityId<T>): T {
        return pa.read(entityId)
    }

    /**
     * Update an existing entity.
     *
     * URL: `PATCH /api/<namespace>/entity/<entityId>`
     *
     * Id comes from the [bo] parameter.
     *
     * @param executor Executor of the operation.
     * @param bo Update.
     *
     * @return BO of the updated entity.
     */
    open fun update(executor: Executor, bo: T): T {
        return pa.update(bo)
    }

    /**
     * Delete an entity.
     *
     * URL: `DELETE /api/<namespace>/entity/<entityId>`
     *
     * @param executor Executor of the operation.
     * @param entityId Id of the entity to delete.
     */
    open fun delete(executor: Executor, entityId: EntityId<T>) {
        pa.delete(entityId)
    }

    /**
     * List all entities of this namespace.
     *
     * URL: `GET /api/<namespace>/entity`
     *
     * @param executor Executor of the operation.
     *
     * @return list of BOs of all entities
     */
    open fun list(executor: Executor): List<T> {
        return pa.list()
    }

    override fun actionWrapper(executor: Executor, func: (Executor, BaseBo) -> Any?, bo: BaseBo): Any? =
        pa.withTransaction {
            super.actionWrapper(executor, func, bo)
        }

    override fun queryWrapper(executor: Executor, func: (Executor, BaseBo) -> Any?, bo: BaseBo): Any? =
        pa.withTransaction {
            super.queryWrapper(executor, func, bo)
        }

}