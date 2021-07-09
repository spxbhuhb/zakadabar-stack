/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.builtin.authorize.SimpleRoleAuthorizationBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.exceptions.Forbidden
import zakadabar.stack.module.module
import zakadabar.stack.util.UUID
import zakadabar.stack.util.default
import kotlin.reflect.KClass

/**
 * Authorizes operations by role names. When a role name is
 * null the operation is rejected. Special role name [PUBLIC]
 * can be used to allow access for everyone.
 *
 * Throws [Forbidden] when rejected.
 */
open class SimpleRoleAuthorizer<T : EntityBo<T>>() : Authorizer<T> {

    companion object {
        val PUBLIC = UUID().toString()
        private val PUBLIC_ID = EntityId<BaseBo>(PUBLIC)

        val LOGGED_IN = UUID().toString()
        private val LOGGED_IN_ID = EntityId<BaseBo>(LOGGED_IN)
    }

    @Suppress("PropertyName")
    val LOGGED_IN = Companion.LOGGED_IN

    @Suppress("PropertyName")
    val PUBLIC = Companion.PUBLIC

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
            this.it()
            initializer = null
        }
    }

    private fun String.toRoleId() = when {
        this == PUBLIC -> PUBLIC_ID
        this == LOGGED_IN -> LOGGED_IN_ID
        else -> roleBl.getByName(this)
    }

    var all: String
        get() = throw NotImplementedError()
        set(value) {
            val roleId = value.toRoleId()
            roles.list = roleId
            roles.read = roleId
            roles.query = roleId
            roles.create = roleId
            roles.update = roleId
            roles.delete = roleId
            roles.action = roleId
        }

    var allReads: String
        get() = throw NotImplementedError()
        set(value) {
            val roleId = value.toRoleId()
            roles.list = roleId
            roles.read = roleId
            roles.query = roleId
        }

    var allWrites: String
        get() = throw NotImplementedError()
        set(value) {
            val roleId = value.toRoleId()
            roles.create = roleId
            roles.update = roleId
            roles.delete = roleId
            roles.action = roleId
        }

    var list: String
        get() = throw NotImplementedError()
        set(value) {
            roles.list = value.toRoleId()
        }

    var read: String
        get() = throw NotImplementedError()
        set(value) {
            roles.read = value.toRoleId()
        }

    var create: String
        get() = throw NotImplementedError()
        set(value) {
            roles.create = value.toRoleId()
        }

    var update: String
        get() = throw NotImplementedError()
        set(value) {
            roles.update = value.toRoleId()
        }

    var delete: String
        get() = throw NotImplementedError()
        set(value) {
            roles.delete = value.toRoleId()
        }

    fun action(actionClass: KClass<out ActionBo<*>>, roleName: String) {
        actionMap[actionClass] = roleName.toRoleId()
    }

    fun query(queryClass: KClass<out QueryBo<*>>, roleName: String) {
        queryMap[queryClass] = roleName.toRoleId()
    }

    override fun authorizeList(executor: Executor) {
        if (roles.list === PUBLIC_ID) return
        if (roles.list === LOGGED_IN_ID && executor.isLoggedIn) return
        roles.list?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeRead(executor: Executor, entityId: EntityId<T>) {
        if (roles.read === PUBLIC_ID) return
        if (roles.read === LOGGED_IN_ID && executor.isLoggedIn) return
        roles.read?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeCreate(executor: Executor, entity: T) {
        if (roles.create === PUBLIC_ID) return
        if (roles.create === LOGGED_IN_ID && executor.isLoggedIn) return
        roles.create?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeUpdate(executor: Executor, entity: T) {
        if (roles.update === PUBLIC_ID) return
        if (roles.update === LOGGED_IN_ID && executor.isLoggedIn) return
        roles.update?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeDelete(executor: Executor, entityId: EntityId<T>) {
        if (roles.delete === PUBLIC_ID) return
        if (roles.delete === LOGGED_IN_ID && executor.isLoggedIn) return
        roles.delete?.let { authorize(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
        actionMap[actionBo::class]
            ?.let {
                if (it === PUBLIC_ID) return
                if (it === LOGGED_IN_ID && executor.isLoggedIn) return
                authorize(executor, it)
            }
            ?: roles.action?.let {
                if (it === PUBLIC_ID) return
                if (it === LOGGED_IN_ID && executor.isLoggedIn) return
                authorize(executor, it)
            }
            ?: throw Forbidden()
    }

    override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
        queryMap[queryBo::class]
            ?.let {
                if (it === PUBLIC_ID) return
                if (it === LOGGED_IN_ID && executor.isLoggedIn) return
                authorize(executor, it)
            }
            ?: roles.query?.let {
                if (it === PUBLIC_ID) return
                if (it === LOGGED_IN_ID && executor.isLoggedIn) return
                authorize(executor, it)
            }
            ?: throw Forbidden()
    }

}
