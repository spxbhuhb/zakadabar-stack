/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementState
import zakadabar.stack.util.after
import kotlin.reflect.KClass

abstract class ZkAppRouting(
    open val defaultLayout: ZkAppLayout,
    open val home: ZkTarget
) {

    companion object {
        var trace = false
    }

    interface ZkTarget {
        val viewName: String
        fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement
    }

    open val targets = mutableMapOf<String, ZkTarget>()

    open var activeLayout by after { defaultLayout }
    open lateinit var activeTarget: ZkElement

    lateinit var nextLayout: ZkAppLayout

    lateinit var navState: ZkNavState

    open fun init() {
        // have to initialize the default layout or the first onPause will set it into
        // the wrong state
        defaultLayout.onCreate()
        defaultLayout.lifeCycleState = ZkElementState.Created
    }

    operator fun ZkTarget.unaryPlus() {
        targets[viewName] = this
    }

    @Suppress("UNCHECKED_CAST") // checked with isInstance
    fun <T : ZkTarget> find(kClass: KClass<T>): List<T> {
        return targets.filterValues { kClass.isInstance(it) }.map { it.value as T }
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
            onMissingRoute()
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

    /**
     * Reports a missing route to the user.
     */
    open fun onMissingRoute() {
        document.body?.innerHTML = stringStore.missingRoute
    }

    /**
     * Builds the local URL for the given target.
     */
    open fun toLocalUrl(target: ZkTarget, subPath: String? = null): String {
        return if (subPath == null) {
            "/${application.locale}/${target.viewName.trimStart('/')}"
        } else {
            "/${application.locale}/${target.viewName.trimStart('/')}/${subPath.trimStart('/')}"
        }
    }

    /**
     * Find a target of the given class. The class may be an interface.
     *
     * @param    kClass      The class to look for
     *
     * @return   First instance of [kClass] from the routing targets.
     *
     * @throws   NoSuchElementException   when there is no such target
     */
    fun <T : Any> first(kClass: KClass<T>): T {
        @Suppress("UNCHECKED_CAST") // checking for class
        return targets.firstNotNullOf { if (kClass.isInstance(it.value)) it.value else null } as T
    }

    /**
     * Find a module of the given class with a selector method called
     * to decided if the module is desired. The class may be an interface.
     *
     * @param    kClass      The class to look for
     * @param    selector    Function to select the instance.
     *
     * @return   First instance of [kClass] from the server modules.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    fun <T : Any> first(kClass: KClass<T>, selector: (T) -> Boolean): T {
        @Suppress("UNCHECKED_CAST") // checking for class
        return targets.firstNotNullOf { if (kClass.isInstance(it.value) && selector(it.value as T)) it.value else null } as T
    }
}