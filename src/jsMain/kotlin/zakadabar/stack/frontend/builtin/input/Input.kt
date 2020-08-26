/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.frontend.builtin.input

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.InputEvent
import org.w3c.dom.events.KeyboardEvent
import zakadabar.stack.frontend.builtin.input.InputClasses.Companion.inputClasses
import zakadabar.stack.frontend.elements.ComplexElement

open class Input(
    private val onEnter: ((String) -> Unit)? = null,
    private val onEscape: (() -> Unit)? = null,
    private val onChange: ((String) -> Unit)? = null,
    private val placeholder: String = ""
) : ComplexElement(
    element = document.createElement("input") as HTMLInputElement
) {

    private val input = element as HTMLInputElement

    var value: String
        get() = input.value
        set(value) {
            input.value = value
        }

    override fun init(): ComplexElement {
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