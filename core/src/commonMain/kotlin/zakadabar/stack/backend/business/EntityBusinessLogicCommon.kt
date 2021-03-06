/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.audit.Auditor
import zakadabar.stack.backend.audit.AuditorProvider
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.AuthorizerDelegate
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.persistence.EntityPersistenceApi
import zakadabar.stack.backend.route.Router
import zakadabar.stack.backend.route.RouterProvider
import zakadabar.stack.backend.validate.SchemaValidator
import zakadabar.stack.backend.validate.Validator
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.module.CommonModule
import kotlin.reflect.KClass

/**
 * Base class for entity backends. Supports CRUD, actions and queries.
 */
abstract class EntityBusinessLogicCommon<T : EntityBo<T>>(
    /**
     * The class of BO this entity backend servers. Namespace is automatically
     * set to the namespace defined for this BO class.
     */
    val boClass: KClass<T>
) : CommonModule {

    abstract fun routerProvider() : RouterProvider

    abstract fun auditorProvider() : AuditorProvider

    /**
     * The namespace this backend serves. Must be unique in a server. Default
     * is the namespace of the BO class.
     */
    abstract val namespace : String

    /**
     * The Persistence API this entity business logic uses.
     */
    abstract val pa: EntityPersistenceApi<T>

    /**
     * Router routes incoming requests to the proper processor function.
     */
    open val router: Router<T> = router { }

    /**
     * Authorizer to call for operation authorization.
     */
    abstract val authorizer: Authorizer<T>

    /**
     * Create and update operations validate the received BO with this validator
     * by calling the `validateCreate` and `validateUpdate` methods.
     */
    open val validator: Validator<T> = SchemaValidator()

    /**
     * Audit records (logs) are created by this auditor.
     */
    open val auditor: Auditor<T> = auditor { }

    fun provider(build: (Authorizer<T>.() -> Unit)? = null) = AuthorizerDelegate(build)

    fun router(build: Router<T>.() -> Unit): Router<T> {
        val r = routerProvider().businessLogicRouter(this)
        r.build()
        return r
    }

    fun auditor(build: Auditor<T>.() -> Unit): Auditor<T> {
        val a = auditorProvider().businessLogicAuditor(this)
        a.build()
        return a
    }

    override fun onModuleLoad() {
        pa.onModuleLoad()
    }

    override fun onModuleStart() {
        authorizer.onModuleStart()
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

    open fun actionWrapper(executor: Executor, func: (Executor, BaseBo) -> BaseBo, bo: BaseBo): BaseBo =

        pa.withTransaction {

            bo as ActionBo<*>

            validator.validateAction(executor, bo)

            authorizer.authorizeAction(executor, bo)

            val response = func(executor, bo)

            auditor.auditAction(executor, bo)

            response

        }

    open fun queryWrapper(executor: Executor, func: (Executor, BaseBo) -> Any, bo: BaseBo): Any =

        pa.withTransaction {

            bo as QueryBo<*>

            validator.validateQuery(executor, bo)

            authorizer.authorizeQuery(executor, bo)

            val response = func(executor, bo)

            auditor.auditQuery(executor, bo)

            response
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


}