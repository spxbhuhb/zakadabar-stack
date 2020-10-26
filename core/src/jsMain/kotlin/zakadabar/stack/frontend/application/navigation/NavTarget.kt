/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application.navigation

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.elements.ComplexElement
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Elements that handle an URL implement this interface and set the value
 * of the path field. When the URL changes, navigation finds the element
 * with the matching path and loads it into the layout.
 */
abstract class NavTarget(val module: String) : ReadOnlyProperty<Any, NavTarget> {

    /** The name of the target, used in the URL. */
    open val name = this::class.simpleName

    /** The layout this element uses. Optional, when not set the default application layout is used */
    open val layout: AppLayout
        get() = FrontendContext.defaultLayout

    /** Adds the element to the layout element list. */
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>): ReadOnlyProperty<Any, NavTarget> {
        layout.navTargets += this
        return this
    }

    /** Enables using the element in a "val e by Element" structure. */
    override fun getValue(thisRef: Any, property: KProperty<*>) = this

    /** Checks if this target is able to handle the given navigation state. */
    fun accepts(state: NavState): Boolean {
        if (state.module != module) return false
        if (state.view != name) return false
        return true
    }

    /** Returns with the element to use for the given state.*/
    abstract fun element(state: NavState): ComplexElement

}

