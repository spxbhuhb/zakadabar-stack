/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButtonStyles
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarStyles
import zakadabar.stack.frontend.resources.ZkIcons

@Deprecated("use ZkAppHandle instead")
open class ZkSideBarTitle(
    private val text: String,
    private val onIconClick: (() -> Unit)? = null,
    private val onTextClick: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {
        className = ZkTitleBarStyles.title

        + ZkIconButton(ZkIcons.notes, cssClass = ZkButtonStyles.transparent, onClick = onIconClick) marginRight 10
        + div { + text }

        on("click") { onTextClick?.invoke() }
        on("mousedown", ::onMouseDown)
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}