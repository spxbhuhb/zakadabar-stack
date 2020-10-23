/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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