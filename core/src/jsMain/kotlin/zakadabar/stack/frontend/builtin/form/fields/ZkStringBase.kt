/*
 * Copyright © 2020, Simplexion, Hungary and contributors
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
package zakadabar.stack.frontend.builtin.form.fields

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import kotlin.reflect.KMutableProperty0

abstract class ZkStringBase<T : DtoBase, VT>(
    form: ZkForm<T>,
    protected val prop: KMutableProperty0<VT>,
    readOnly: Boolean = false,
    label: String? = null
) : ZkFieldBase<T, VT>(
    form = form,
    propName = prop.name,
    readOnly = readOnly,
    label = label
) {

    protected val input = document.createElement("input") as HTMLInputElement

    abstract fun getPropValue(): String

    abstract fun setPropValue(value: String)

    override fun buildFieldValue() {

        if (readOnly) {
            input.readOnly = true
            input.className = ZkFormStyles.disabledString
        } else {
            input.className = ZkFormStyles.text
        }

        input.value = getPropValue()

        on(input, "input") {
            touched = true
            setPropValue(input.value)
            form.validate()
        }

        focusEvents(input)

        + input
    }

    override fun focusValue() {
        input.focus()
    }

}