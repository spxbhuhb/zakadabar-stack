/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigation

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.builtin.layout.AppLayout
import zakadabar.stack.frontend.elements.ComplexElement
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Elements that handle an URL path implement this interface and set the value
 * of the path field. When the URL changes, navigation finds the element
 * with the matching path and loads it into the layout.
 */
abstract class Route : ReadOnlyProperty<Any, Route> {

    /** The layout this element uses. Optional, when not set the default
     * application layout is used **/
    val layout: AppLayout
        get() = FrontendContext.defaultLayout

    /** Adds the element to the layout element list.**/
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>): ReadOnlyProperty<Any, Route> {
        layout.routes += this
        return this
    }

    /** Enables using the element in a "val e by Element" structure. **/
    override fun getValue(thisRef: Any, property: KProperty<*>) = this

    abstract fun element(newState : NavState) : ComplexElement

}

