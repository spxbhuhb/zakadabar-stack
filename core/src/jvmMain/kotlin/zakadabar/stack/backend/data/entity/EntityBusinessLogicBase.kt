/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.entity

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.audit.Auditor
import zakadabar.stack.backend.audit.LogAuditor
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.server
import zakadabar.stack.backend.util.executor
import zakadabar.stack.backend.validate.SchemaValidator
import zakadabar.stack.backend.validate.Validator
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for entity backends. Supports CRUD, queries and BLOBs.
 */
abstract class EntityBusinessLogicBase<T : EntityBo<T>> : BackendModule {

    /**
     * The class of BO this entity backend servers. Namespace is automatically
     * set to the namespace defined for this BO class.
     */
    abstract val boClass: KClass<T>

    /**
     * The namespace this backend serves. Must be unique in a server. Default
     * is the namespace of the BO class.
     */
    val namespace
        get() = (boClass.companionObject !!.objectInstance as EntityBoCompanion<*>).boNamespace

    /**
     * The Persistence API this entity business logic uses.
     */
    abstract val pa: EntityPersistenceApiBase<T>

    /**
     * Logger to use when logging is enabled. Name is [namespace].
     */
    val logger by lazy { LoggerFactory.getLogger(namespace) !! }

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
     * Audit records (logs) are creted by this auditor.
     */
    open val auditor: Auditor<T> = LogAuditor<T>(logger)

    /**
     * Adds cache control directive to GET requests, except BLOB content.
     * Default implementation calls the function with the same name from the
     * server. The function in the server sets the Cache-Control header to the
     * value specified by the server settings. Default is "no-cache, no-store".
     */
    open fun apiCacheControl(call: ApplicationCall) {
        server.apiCacheControl(call)
    }

    override fun onModuleLoad() {
        pa.onModuleLoad()
    }

    override fun onModuleStart() {
        authorizer.onModuleStart()
    }

    override fun onInstallRoutes(route: Route) {
        with(route) {
            route("$namespace/entity") {

                get("/{rid?}") {
                    call.parameters["rid"]?.let { readWrapper(call, it) } ?: listWrapper(call)
                }

                post {
                    createWrapper(call)
                }

                patch("/{rid}") {
                    updateWrapper(call)
                }

                delete("/{rid}") {
                    deleteWrapper(call)
                }
            }
        }
    }

    suspend fun listWrapper(call: ApplicationCall) {

        val executor = call.executor()

        apiCacheControl(call)

        authorizer.authorizeList(executor)

        call.respond(list(executor) as Any)

        auditor.auditList(executor)

    }

    suspend fun readWrapper(call: ApplicationCall, id: String) {

        val id = call.parameters["rid"] ?: throw BadRequestException("missing id")

        val executor = call.executor()
        val entityId = EntityId<T>(id)

        authorizer.authorizeRead(executor, entityId)

        apiCacheControl(call)

        call.respond(read(call, executor, entityId) as Any)

        auditor.auditRead(executor, entityId)

    }

    suspend fun createWrapper(call: ApplicationCall) {

        val executor = call.executor()
        val request = call.receive(boClass)

        validator.validateCreate(executor, request)
        authorizer.authorizeCreate(executor, request)

        val response = create(executor, request)

        call.respond(response as Any)

        auditor.auditCreate(executor, response)

    }

    suspend fun updateWrapper(call: ApplicationCall) {

        val executor = call.executor()
        val request = call.receive(boClass)

        validator.validateUpdate(executor, request)
        authorizer.authorizeUpdate(executor, request)

        val response = update(executor, request)

        call.respond(response as Any)

        auditor.auditUpdate(executor, response)

    }

    suspend fun deleteWrapper(call: ApplicationCall) {

        val id = call.parameters["rid"] ?: throw BadRequestException("missing id")

        val entityId = EntityId<T>(id)

        val executor = call.executor()

        authorizer.authorizeDelete(executor, entityId)

        call.respond(delete(executor, entityId) as Any)

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
     * Read an entity with access to Ktor's application call. The
     * default implementation simply calls read without [call].
     *
     * URL: `GET /api/<namespace>/entity/<entityId>`
     *
     * @param call     Ktor's [ApplicationCall].
     * @param executor Executor of the operation.
     * @param entityId The id of the entity to read.
     *
     * @return BO of the entity
     */
    open fun read(call: ApplicationCall, executor: Executor, entityId: EntityId<T>): T {
        return read(executor, entityId)
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