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

import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.input.InputClasses.Companion.inputClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.OptionalElement
import zakadabar.stack.frontend.elements.SimpleElement
import zakadabar.stack.util.PublicApi

@PublicApi
open class SingleLineInput : ComplexElement() {

    var value: String
        get() = input.value
        set(value) {
            input.value = value
        }

    open val prefixIcon: SimpleElement = OptionalElement()
    open val input = Input(::onEnter, ::onEscape)
    open val approveIcon = Icons.check.complex18 { approve(value) }
    open val cancelIcon = Icons.close.complex18 { cancel() }

    override fun init(): SingleLineInput {

        val classes = inputClasses

        className = classes.inputContentElement

        build {
            + prefixIcon
            + input
            + row(classes.inputPostfix) {
                + approveIcon.withClass(classes.inputPostfixIcon, classes.approveFill)
                + cancelIcon.withClass(classes.inputPostfixIcon, classes.cancelFill)
            }
        }

        return this
    }

    open fun approve(value: String) = Unit

    open fun cancel() {
        hide()
    }

    override fun focus() = input.focus()

    open fun onEnter(value: String) = approve(value)

    open fun onEscape() = cancel()

}