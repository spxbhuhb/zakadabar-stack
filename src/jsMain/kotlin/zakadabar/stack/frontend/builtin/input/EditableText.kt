/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input

import zakadabar.stack.frontend.FrontendContext.theme
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.simple.SimpleText
import zakadabar.stack.frontend.builtin.util.SwitchView
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.SwitchableElement
import zakadabar.stack.util.PublicApi

/**
 * A single line text with an edit icon. When the user clicks on the
 * edit icon the text is replaced with an input box and the icon is
 * replaced with an ok/cancel icon.
 */
@PublicApi
open class EditableText(
    var text: String,
    private val onApprove: (newValue: String) -> Unit = { }
) : SwitchView() {

    init {
        views += { ReadView() }
        views += { EditView() }
    }

    inner class ReadView : SwitchableElement() {

        override fun init(): ComplexElement {

            className = coreClasses.row

            this += SimpleText(text).marginRight(theme.margin)
            this += Icons.edit.complex16 { switchView.next() }

            return this
        }

    }

    inner class EditView : SwitchableElement() {

        private val input = Input(::onApprove, ::onCancel)

        override fun init(): ComplexElement {

            className = coreClasses.row

            input.value = text

            this += input
            this += Icons.check.complex16 { onApprove(input.value) }
            this += Icons.close.complex16 { switchView.previous() }

            return this
        }

        private fun onApprove(value: String) {
            this@EditableText.onApprove(value)
            text = value
            switchView.previous()
        }

        private fun onCancel() {
            switchView.previous()
        }
    }

}