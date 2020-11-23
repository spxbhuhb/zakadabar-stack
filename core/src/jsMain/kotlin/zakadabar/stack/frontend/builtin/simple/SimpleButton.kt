/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.simple

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.simple.SimpleClasses.Companion.simpleClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.util.PublicApi

/**
 * A simple clickable button.
 *
 * @property  text     The label of the button
 * @property  onClick  The function to execute when the button is clicked.
 *
 * @since  2020.9.14
 */
@PublicApi
class SimpleButton(
    private val text: String,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    override fun init(): ZkElement {

        this cssClass simpleClasses.button build {
            + SimpleText(text)
        }

        on("click", onClick)
        on("mousedown", ::onMouseDown)

        return this
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}