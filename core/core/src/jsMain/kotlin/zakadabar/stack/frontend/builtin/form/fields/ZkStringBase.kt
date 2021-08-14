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
package zakadabar.core.frontend.builtin.form.fields

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.core.frontend.builtin.form.ZkFormStyles
import zakadabar.core.frontend.util.plusAssign
import kotlin.reflect.KMutableProperty0

abstract class ZkStringBase<VT>(
    context : ZkFieldContext,
    protected val prop: KMutableProperty0<VT>,
    label: String? = null
) : ZkFieldBase<VT>(
    context = context,
    propName = prop.name,
    label = label
) {

    protected val input = document.createElement("input") as HTMLInputElement

    override var readOnly = context.readOnly
        set(value) {
            input.disabled = value
            field = value
        }

    abstract fun getPropValue(): String

    abstract fun setPropValue(value: String)

    override fun buildFieldValue() {

        if (readOnly) {
            input.readOnly = true
            input.classList += ZkFormStyles.disabledString
        } else {
            input.classList += ZkFormStyles.text
        }

        input.value = getPropValue()

        on(input, "input") {
            touched = true
            setPropValue(input.value)
            context.validate()
        }

        focusEvents(input)

        + input
    }

    override fun focusValue() {
        input.focus()
    }

}