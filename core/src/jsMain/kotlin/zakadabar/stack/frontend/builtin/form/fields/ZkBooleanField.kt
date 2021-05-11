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

import org.w3c.dom.events.KeyboardEvent
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.zkFormStyles
import zakadabar.stack.frontend.builtin.input.ZkCheckBox
import zakadabar.stack.frontend.resources.ZkIcons
import kotlin.reflect.KMutableProperty0

open class ZkBooleanField<T : DtoBase>(
    form: ZkForm<T>,
    val prop: KMutableProperty0<Boolean>
) : ZkFieldBase<T, Boolean>(
    form = form,
    propName = prop.name
) {

    private val checkbox = ZkCheckBox(ZkIcons.check)

    override var readOnly: Boolean = (form.mode == ZkElementMode.Read)
        set(value) {
            checkbox.disabled = value
            field = value
        }

    override fun buildFieldValue() {
        + div(zkFormStyles.booleanField) {

            buildElement.tabIndex = 0

            val value: Boolean = prop.get()

            if (readOnly) checkbox.checkbox.readOnly = true

            checkbox.checked = value

            on(checkbox.checkbox, "change") {
                prop.set(checkbox.checked)
                touched = true
                form.validate()
            }

            on(buildElement, "keypress") {
                it as KeyboardEvent
                when (it.key) {
                    "Enter", " " -> {
                        checkbox.checked = ! checkbox.checked
                        touched = true
                    }
                }
            }

            + checkbox
        }
    }

    override fun mandatoryMark() {
        // do not show mandatory mark for checkboxes, in most cases it is
        // useless as they always have a value (true or false)
    }
}