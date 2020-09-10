/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.elements.ComplexElement

open class ComplexIcon(
    val icon: String,
    private val onClick: (() -> Unit)? = null
) : ComplexElement() {

    override fun init(): ComplexElement {
        element.innerHTML = icon
        on("click", onClick)
        on("mousedown", ::onMouseDown)
        return this
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}