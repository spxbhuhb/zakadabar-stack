/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.backend.util.default
import zakadabar.stack.data.builtin.authorize.SimpleRoleAuthorizationBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor

/**
 * Authorizes operations by role names. When a role name is
 * null the operation is rejected.
 *
 * Throws [Forbidden] when rejected.
 */
class SimpleRoleAuthorizer<T : EntityBo<T>>() : Authorizer<T> {

    private val roles: SimpleRoleAuthorizationBo = default { }

    private var initializer: (SimpleRoleAuthorizer<T>.() -> Unit)? = null

    constructor(initializer: SimpleRoleAuthorizer<T>.() -> Unit) : this() {
        this.initializer = initializer
    }

    override fun onModuleStart() {
        initializer?.let {
            it()
            initializer = null
        }
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

}
