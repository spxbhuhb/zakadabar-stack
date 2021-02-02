/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.plusAssign

open class ZkIcon(
    val icon: String,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    constructor(source: IconSource, onClick: (() -> Unit)? = null) : this(source.svg(24), onClick)

    override fun init(): ZkElement {
        classList += ZkIconStyles.icon
        element.innerHTML = icon
        if (onClick != null) {
            on("click", onClick)
            on("mousedown", ::onMouseDown)
        }
        return this
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}