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
import org.w3c.dom.events.KeyboardEvent
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import kotlin.reflect.KMutableProperty0

class ValidatedBoolean<T : DtoBase>(
    form: ZkForm<T>,
    val prop: KMutableProperty0<Boolean>
) : FormField<T, Boolean>(
    form = form,
    propName = prop.name
) {

    private val checkbox = document.createElement("input") as HTMLInputElement

    override fun buildFieldValue() {
        + div(ZkFormStyles.checkbox) {
            checkbox.type = "checkbox"
            checkbox.id = "zk-$id-checkbox"

            currentElement.tabIndex = 0

            val value: Boolean = prop.get()

            if (readOnly) checkbox.readOnly = true

            checkbox.checked = value

            on(checkbox, "change") { _ ->
                prop.set(checkbox.checked)
                form.validate()
            }

            on(currentElement, "keypress") { event ->
                event as KeyboardEvent
                when (event.key) {
                    "Enter", " " -> checkbox.checked = ! checkbox.checked
                }
            }

            + checkbox
        }
    }
}