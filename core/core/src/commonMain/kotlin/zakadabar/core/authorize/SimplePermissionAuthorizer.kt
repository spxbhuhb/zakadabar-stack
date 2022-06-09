/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.data.BaseBo
import zakadabar.core.data.ActionBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo
import zakadabar.core.exception.Forbidden
import zakadabar.core.module.module
import zakadabar.core.util.UUID
import zakadabar.core.util.default
import kotlin.reflect.KClass

/**
 * Authorizes operations by role names. When a role name is
 * null the operation is rejected. Special role name [PUBLIC]
 * can be used to allow access for everyone.
 *
 * Throws [Forbidden] when rejected.
 */
open class SimplePermissionAuthorizer<T : BaseBo>(
    val permissions: SimplePermissionAuthorizationBo = default {}
) : BusinessLogicAuthorizer<T> {

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

    private val actionMap = mutableMapOf<KClass<out BaseBo>, EntityId<out BaseBo>>()

    private val queryMap = mutableMapOf<KClass<out BaseBo>, EntityId<out BaseBo>>()

    private var initializer: (SimplePermissionAuthorizer<T>.() -> Unit)? = null

    constructor(initializer: SimplePermissionAuthorizer<T>.() -> Unit) : this() {
        this.initializer = initializer
    }

    private val permissionBl by module<PermissionBlProvider>()

    override fun onModuleStart() {
        initializer?.let {
            this.it()
            initializer = null
        }
    }

    private fun String.toPermissionId() =  when {
        this == PUBLIC -> PUBLIC_ID
        this == LOGGED_IN -> LOGGED_IN_ID
        else -> permissionBl.getByName(this)
    }

    var all: String
        get() = throw NotImplementedError()
        set(value) {
            val permissionId = value
            permissions.list = permissionId
            permissions.read = permissionId
            permissions.query = permissionId
            permissions.create = permissionId
            permissions.update = permissionId
            permissions.delete = permissionId
            permissions.action = permissionId
        }

    var allReads: String
        get() = throw NotImplementedError()
        set(value) {
            val roleId = value
            permissions.list = roleId
            permissions.read = roleId
            permissions.query = roleId
        }

    var allWrites: String
        get() = throw NotImplementedError()
        set(value) {
            val roleId = value
            permissions.create = roleId
            permissions.update = roleId
            permissions.delete = roleId
            permissions.action = roleId
        }

    var list: String
        get() = throw NotImplementedError()
        set(value) {
            permissions.list = value
        }

    var read: String
        get() = throw NotImplementedError()
        set(value) {
            permissions.read = value
        }

    var create: String
        get() = throw NotImplementedError()
        set(value) {
            permissions.create = value
        }

    var update: String
        get() = throw NotImplementedError()
        set(value) {
            permissions.update = value
        }

    var delete: String
        get() = throw NotImplementedError()
        set(value) {
            permissions.delete = value
        }

    fun action(actionClass: KClass<out ActionBo<*>>, permissionName: String) {
        actionMap[actionClass] = permissionName.toPermissionId()
    }

    fun query(queryClass: KClass<out QueryBo<*>>, permissionName: String) {
        queryMap[queryClass] = permissionName.toPermissionId()
    }

    override fun authorizeList(executor: Executor) {
        if (permissions.list?.toPermissionId() === PUBLIC_ID) return
        if (permissions.list?.toPermissionId() === LOGGED_IN_ID && executor.isLoggedIn) return
        permissions.list?.let { authorizeByPermission(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeRead(executor: Executor, entityId: EntityId<T>) {
        if (permissions.read?.toPermissionId() === PUBLIC_ID) return
        if (permissions.read?.toPermissionId() === LOGGED_IN_ID && executor.isLoggedIn) return
        permissions.read?.let { authorizeByPermission(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeCreate(executor: Executor, entity: T) {
        if (permissions.create?.toPermissionId() === PUBLIC_ID) return
        if (permissions.create?.toPermissionId() === LOGGED_IN_ID && executor.isLoggedIn) return
        permissions.create?.let { authorizeByPermission(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeUpdate(executor: Executor, entity: T) {
        if (permissions.update?.toPermissionId() === PUBLIC_ID) return
        if (permissions.update?.toPermissionId() === LOGGED_IN_ID && executor.isLoggedIn) return
        permissions.update?.let { authorizeByPermission(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeDelete(executor: Executor, entityId: EntityId<T>) {
        if (permissions.delete?.toPermissionId() === PUBLIC_ID) return
        if (permissions.delete?.toPermissionId() === LOGGED_IN_ID && executor.isLoggedIn) return
        permissions.delete?.let { authorizeByPermission(executor, it) } ?: throw Forbidden()
    }

    override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
        actionMap[actionBo::class]
            ?.let {
                if (it === PUBLIC_ID) return
                if (it === LOGGED_IN_ID && executor.isLoggedIn) return
                authorizeByPermission(executor, it)
            }
            ?: permissions.action?.let {
                if (it.toPermissionId() === PUBLIC_ID) return
                if (it.toPermissionId() === LOGGED_IN_ID && executor.isLoggedIn) return
                authorizeByPermission(executor, it)
            }
            ?: throw Forbidden()
    }

    override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
        queryMap[queryBo::class]
            ?.let {
                if (it === PUBLIC_ID) return
                if (it === LOGGED_IN_ID && executor.isLoggedIn) return
                authorizeByPermission(executor, it)
            }
            ?: permissions.query?.let {
                if (it.toPermissionId() === PUBLIC_ID) return
                if (it.toPermissionId() === LOGGED_IN_ID && executor.isLoggedIn) return
                authorizeByPermission(executor, it)
            }
            ?: throw Forbidden()
    }

}
