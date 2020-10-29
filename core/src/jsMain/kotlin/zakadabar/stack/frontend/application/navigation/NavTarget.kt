/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application.navigation

import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.application.AppRouting
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.elements.ZkElement
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class NavTargetProvider(private val func: (state: NavState) -> ZkElement) {

    operator fun provideDelegate(thisRef: AppRouting, prop: KProperty<*>): ReadOnlyProperty<Any, NavTarget> {
        val target = NavTarget(thisRef.appModule, prop.name, func)
        if (target.layout !in Application.layouts) Application.layouts += target.layout
        target.layout.navTargets += target
        return target
    }

}

/**
 * Elements that handle an URL implement this interface and set the value
 * of the path field. When the URL changes, navigation finds the element
 * with the matching path and loads it into the layout.
 */
class NavTarget(
    val module: String,
    val name: String,
    val element: (state: NavState) -> ZkElement
) : ReadOnlyProperty<Any, NavTarget> {

    /** The layout this element uses. Optional, when not set the default application layout is used */
    val layout: AppLayout
        get() = Application.defaultLayout

    /** Enables using the element in a "val e by Element" structure. */
    override fun getValue(thisRef: Any, property: KProperty<*>) = this

    /** Checks if this target is able to handle the given navigation state. */
    fun accepts(state: NavState): Boolean {
        if (state.module != module) return false
        if (state.view != name) return false
        return true
    }

}