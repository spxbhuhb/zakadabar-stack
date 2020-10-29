/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.InputEvent
import org.w3c.dom.events.KeyboardEvent
import zakadabar.stack.frontend.builtin.input.InputClasses.Companion.inputClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.plusAssign

open class Input(
    private val onEnter: ((String) -> Unit)? = null,
    private val onEscape: (() -> Unit)? = null,
    private val onChange: ((String) -> Unit)? = null,
    private val placeholder: String = ""
) : ZkElement(
    element = document.createElement("input") as HTMLInputElement
) {

    private val input = element as HTMLInputElement

    var value: String
        get() = input.value
        set(value) {
            input.value = value
        }

    override fun init(): ZkElement {
        classList += inputClasses.inputInput

        input.placeholder = placeholder

        on("keydown", ::onKeyDown)
        on("input", ::onInput)
        return this
    }

    private fun onKeyDown(event: Event) {
        event as KeyboardEvent

        if (event.key == "Enter" && onEnter != null) {
            event.stopPropagation()
            event.preventDefault()
            onEnter.invoke(input.value)
            return
        }

        if (event.key == "Escape" && onEscape != null) {
            event.stopPropagation()
            event.preventDefault()
            onEscape.invoke()
            return
        }

    }

    private fun onInput(event: Event) {
        event as InputEvent
        onChange?.invoke(input.value)
    }
}