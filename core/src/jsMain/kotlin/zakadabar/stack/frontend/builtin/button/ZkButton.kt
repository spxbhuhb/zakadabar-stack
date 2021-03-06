/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.util.PublicApi

/**
 * A simple clickable button with a label.
 *
 * @property  label     The label of the button
 * @property  onClick  The function to execute when the button is clicked.
 *
 * @since  2020.9.14
 */
@PublicApi
class ZkButton(
    private val text: String,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {
        className = ZkButtonStyles.button

        + text

        on("click") { _ -> onClick?.invoke() }
        on("mousedown", ::onMouseDown)
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}