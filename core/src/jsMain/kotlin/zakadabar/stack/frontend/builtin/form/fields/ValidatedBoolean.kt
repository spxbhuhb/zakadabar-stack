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
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.elements.ZkElement
import kotlin.reflect.KMutableProperty0

class ValidatedBoolean<T : DtoBase>(
    private val form: ZkForm<T>,
    private val prop: KMutableProperty0<Boolean>
) : FormField<Boolean>(
    element = document.createElement("input") as HTMLInputElement
) {

    private val checkbox = element as HTMLInputElement

    override fun init(): ZkElement {
        checkbox.type = "checkbox"

        val value: Boolean = prop.get()

        if (readOnly) checkbox.readOnly = true

        checkbox.checked = value

        on("change") { _ ->
            prop.set(this.checkbox.checked)
            form.validate()
        }

        return this
    }

    override fun onValidated(report: ValidityReport) {
        val fails = report.fails[prop.name]
        if (fails == null) {
            isValid = true
            element.style.backgroundColor = "white"
        } else {
            isValid = false
            element.style.backgroundColor = "red"
        }
    }

}