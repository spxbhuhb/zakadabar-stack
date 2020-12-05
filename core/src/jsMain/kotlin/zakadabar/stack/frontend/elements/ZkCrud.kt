/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.application.AppRouting
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.application.NavState
import zakadabar.stack.frontend.builtin.util.NYI

/**
 * Provides common functions used in most CRUD implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkCrud<T> : AppRouting.ZkTarget {

    private var _module = "default"

    override var module
        get() = _module
        set(value) {
            _module = value
        }

    private var _viewPrefix = "/${this::class.simpleName}"

    override var viewPrefix
        get() = _viewPrefix
        set(value) {
            _viewPrefix = value
        }

    val allPath
        get() = "/$_module$_viewPrefix/all"
    val createPath
        get() = "/$_module$_viewPrefix/create"
    val readPath
        get() = "/$_module$_viewPrefix/read"
    val updatePath
        get() = "/$_module$_viewPrefix/update"
    val deletePath
        get() = "/$_module$_viewPrefix/delete"

    open fun openAll() = Application.changeNavState(allPath)
    open fun openCreate() = Application.changeNavState(createPath)
    open fun openRead(recordId: RecordId<T>) = Application.changeNavState(readPath, "id=$recordId")
    open fun openUpdate(recordId: RecordId<T>) = Application.changeNavState(updatePath, "id=$recordId")
    open fun openDelete(recordId: RecordId<T>) = Application.changeNavState(deletePath, "id=$recordId")

    override fun route(routing: AppRouting, state: NavState) = when (state.urlPath) {
        allPath -> all()
        createPath -> create()
        readPath -> read(state.recordId)
        updatePath -> update(state.recordId)
        deletePath -> delete(state.recordId)
        else -> routeQuery(routing, state)
    }

    open fun routeQuery(routing: AppRouting, state: NavState): ZkElement = NYI()

    open fun all(): ZkElement = NYI("crud all")
    open fun create(): ZkElement = NYI("crud create")
    open fun read(recordId: Long): ZkElement = NYI("crud read")
    open fun update(recordId: Long): ZkElement = NYI("crud update")
    open fun delete(recordId: Long): ZkElement = NYI("crud delete")
}