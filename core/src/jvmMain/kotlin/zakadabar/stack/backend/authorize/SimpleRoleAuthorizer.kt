/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.backend.module
import zakadabar.stack.backend.util.default
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.builtin.authorize.SimpleRoleAuthorizationBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass

/**
 * Authorizes operations by role names. When a role name is
 * null the operation is rejected.
 *
 * Throws [Forbidden] when rejected.
 */
open class SimpleRoleAuthorizer<T : EntityBo<T>>() : Authorizer<T> {

    private val roles: SimpleRoleAuthorizationBo = default { }

    private val actionMap = mutableMapOf<KClass<out BaseBo>, EntityId<out BaseBo>>()

    private val queryMap = mutableMapOf<KClass<out BaseBo>, EntityId<out BaseBo>>()

    private var initializer: (SimpleRoleAuthorizer<T>.() -> Unit)? = null

    constructor(initializer: SimpleRoleAuthorizer<T>.() -> Unit) : this() {
        this.initializer = initializer
    }

    private val roleBl by module<RoleBlProvider>()

    override fun onModuleStart() {
        initializer?.let {
            it()
            initializer = null
        }
    }

    var all : String
        get() = throw NotImplementedError()
        set(value) {
            val roleId = roleBl.getByName(value)
            roles.list = roleId
            roles.read = roleId
            roles.query = roleId
            roles.create = roleId
            roles.update = roleId
            roles.action = roleId
        }

    var allReads: String
        get() = throw NotImplementedError()
        set(value) {
            val roleId = roleBl.getByName(value)
            roles.list = roleId
            roles.read = roleId
            roles.query = roleId
        }

    var allWrites: String
        get() = throw NotImplementedError()
        set(value) {
            val roleId = roleBl.getByName(value)
            roles.create = roleId
            roles.update = roleId
            roles.action = roleId
        }

    var list: String
        get() = throw NotImplementedError()
        set(value) {
            roles.list = roleBl.getByName(value)
        }

    var read: String
        get() = throw NotImplementedError()
        set(value) {
            roles.read = roleBl.getByName(value)
        }

    var create: String
        get() = throw NotImplementedError()
        set(value) {
            roles.create = roleBl.getByName(value)
        }

    var update: String
        get() = throw NotImplementedError()
        set(value) {
            roles.update = roleBl.getByName(value)
        }

    var delete: String
        get() = throw NotImplementedError()
        set(value) {
            roles.delete = roleBl.getByName(value)
        }

    fun action(actionClass: KClass<out ActionBo<*>>, roleName: String) {
        actionMap[actionClass] = roleBl.getByName(roleName)
    }

    fun query(queryClass: KClass<out QueryBo<*>>, roleName: String) {
        queryMap[queryClass] = roleBl.getByName(roleName)
    }

    override fun authorizeList(executor: Executor) {
        roles.list?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeRead(executor: Executor, entityId: EntityId<T>) {
        roles.read?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeCreate(executor: Executor, entity: T) {
        roles.create?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeUpdate(executor: Executor, entity: T) {
        roles.update?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeDelete(executor: Executor, entityId: EntityId<T>) {
        roles.delete?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
        actionMap[actionBo::class]?.let { authorize(executor, it) }
            ?: roles.action?.let { authorize(executor, it) }
            ?: throw Forbidden()
    }

    override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
        queryMap[queryBo::class]?.let { authorize(executor, it) }
            ?: roles.query?.let { authorize(executor, it) }
            ?: throw Forbidden()
    }

}
