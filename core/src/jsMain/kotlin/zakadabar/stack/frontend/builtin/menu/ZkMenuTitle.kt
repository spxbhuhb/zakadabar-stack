/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.elements.ZkElement

open class ZkMenuTitle(
    private val text: String,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    override fun init() = build {
        className = ZkMenuStyles.title
        + text

        on("click", onClick)
        on("mousedown", ::onMouseDown)

    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}