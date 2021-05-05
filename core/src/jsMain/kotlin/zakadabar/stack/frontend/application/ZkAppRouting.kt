/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementState

abstract class ZkAppRouting(
    private val defaultLayout: ZkAppLayout,
    private val home: ZkTarget
) {

    companion object {
        var trace = false
    }

    interface ZkTarget {
        val viewName: String
        fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement
    }

    private val targets = mutableMapOf<String, ZkTarget>()

    private var activeLayout = defaultLayout
    private lateinit var activeTarget: ZkElement

    lateinit var nextLayout: ZkAppLayout

    lateinit var navState: ZkNavState

    init {
        // have to initialize the default layout or the first onPause will set it into
        // the wrong state
        defaultLayout.onCreate()
        defaultLayout.lifeCycleState = ZkElementState.Created
    }

    operator fun ZkTarget.unaryPlus() {
        targets[viewName] = this
    }

    open fun onNavStateChange(state: ZkNavState) {

        nextLayout = defaultLayout // when not specified, use the default layout

        val nextTarget = if (state.viewName.isEmpty()) {
            home.route(this, state)
        } else {
            targets[state.viewName]?.route(this, state) ?: route(state)
        }

        if (nextTarget == null) {
            println("AppRouting.onNavStateChange: missing route for $state")
            return
        }

        if (nextLayout != activeLayout) {
            activeLayout.onPause()
            activeLayout = nextLayout
        }

        activeTarget = nextTarget

        navState = state

        if (activeLayout.lifeCycleState == ZkElementState.Initialized) {
            activeLayout.onCreate()
            activeLayout.lifeCycleState = ZkElementState.Created
        }

        activeLayout.resume(state, activeTarget)

        if (trace) {
            println("AppRouting.onNavStateChange  layout=${activeLayout.name}  class=${nextTarget::class.simpleName}  $state")
        }
    }

    open fun route(state: ZkNavState): ZkElement? = null

}