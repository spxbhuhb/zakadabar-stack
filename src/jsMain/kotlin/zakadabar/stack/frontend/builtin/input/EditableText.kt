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