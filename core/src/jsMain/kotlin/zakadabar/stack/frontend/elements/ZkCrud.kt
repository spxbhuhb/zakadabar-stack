/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.application.AppRouting
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.application.NavState
import zakadabar.stack.frontend.builtin.util.NYI

/**
 * Provides common functions used in most CRUD implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkCrud(
    final override val module: String,
    final override val viewPrefix: String
) : AppRouting.ZkTarget {

    val allPath = "$module$viewPrefix/all"
    val createPath = "$module$viewPrefix/create"
    val readPath = "$module$viewPrefix/read"
    val updatePath = "$module$viewPrefix/update"
    val deletePath = "$module$viewPrefix/delete"

    open fun openAll() = Application.changeNavState(allPath)
    open fun openCreate() = Application.changeNavState(createPath)
    open fun openRead(recordId: Long) = Application.changeNavState("$readPath?id=$recordId")
    open fun openUpdate(recordId: Long) = Application.changeNavState("$updatePath?id=$recordId")
    open fun openDelete(recordId: Long) = Application.changeNavState("$deletePath?id=$recordId")

    override fun route(routing: AppRouting, state: NavState) {
        routing.target = when (state.view) {
            allPath -> all()
            createPath -> create()
            readPath -> read(state.recordId)
            updatePath -> update(state.recordId)
            deletePath -> delete(state.recordId)
            else -> routeQuery(routing, state)
        }
    }

    open fun routeQuery(routing: AppRouting, state: NavState): ZkElement = NYI()

    open fun all(): ZkElement = NYI()
    open fun create(): ZkElement = NYI()
    open fun read(recordId: Long): ZkElement = NYI()
    open fun update(recordId: Long): ZkElement = NYI()
    open fun delete(recordId: Long): ZkElement = NYI()
}