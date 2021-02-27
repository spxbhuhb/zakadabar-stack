/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.elements.ZkElement

open class ZkSideBarItem(
    private val text: String,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    override fun init() = build {
        className = ZkSideBarStyles.item

        + div {
            + text
        }

        on("click", onClick)
        on("mousedown", ::onMouseDown)

    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}