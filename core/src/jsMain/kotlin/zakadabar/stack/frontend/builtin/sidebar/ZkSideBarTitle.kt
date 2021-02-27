/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.button.ZkButtonStyles
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.Icons

open class ZkSideBarTitle(
    private val text: String,
    private val onIconClick: (() -> Unit)? = null,
    private val onTextClick: (() -> Unit)? = null
) : ZkElement() {

    override fun init() = build {
        className = ZkSideBarStyles.title

        + ZkIconButton(Icons.notes, cssClass = ZkButtonStyles.transparent, onClick = onIconClick) marginRight 10
        + text

        on("click", onTextClick)
        on("mousedown", ::onMouseDown)
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}