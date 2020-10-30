/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import zakadabar.stack.frontend.builtin.util.NYI
import zakadabar.stack.frontend.elements.ZkElement

abstract class AppRouting(
    private val defaultLayout: AppLayout
) {

    interface ZkTarget {
        val module: String
        val viewPrefix: String
        fun route(routing: AppRouting, state: NavState)
    }

    private val targets = mutableMapOf<String, ZkTarget>()

    private lateinit var activeLayout: AppLayout

    lateinit var layout: AppLayout
    lateinit var target: ZkElement

    operator fun ZkTarget.unaryPlus() {
        require(viewPrefix.startsWith('/'))
        targets["$module$viewPrefix"] = this
    }

    internal fun onNavStateChange(state: NavState) {

        layout = defaultLayout
        target = NYI()

        val registered = targets["${state.module}${state.view}"]

        if (registered != null) {
            registered.route(this, state)
        } else {
            route(state)
        }

        if (layout != activeLayout) {
            activeLayout.pause()
            activeLayout = layout
        }

        activeLayout.resume(state, target)
    }

    open fun route(state: NavState) {}

}