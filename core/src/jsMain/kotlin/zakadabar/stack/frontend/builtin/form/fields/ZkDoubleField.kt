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

open class ZkDoubleField<T : DtoBase>(
    form: ZkForm<T>,
    private val prop: KMutableProperty0<Double>
) : ZkFieldBase<T, Double>(
    form = form,
    propName = prop.name
) {

    private val input = document.createElement("input") as HTMLInputElement

    override fun buildFieldValue() {
        input.className = ZkFormStyles.text

        if (readOnly) input.readOnly = true

        val value = prop.get()

        if (value.isNaN()) {
            input.value = ""
        } else {
            input.value = prop.get().toString()
        }

        on(input, "input") {
            touched = true

            val iv = input.value.toDoubleOrNull()

            if (iv == null) {
                invalidInput = true
            } else {
                invalidInput = false
                prop.set(iv)
            }

            form.validate()
        }

        focusEvents(input)

        + input
    }

    override fun focusValue() {
        input.focus()
    }
}