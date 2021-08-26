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
package zakadabar.core.browser.field

import org.w3c.dom.events.KeyboardEvent
import zakadabar.core.browser.input.ZkCheckBox
import zakadabar.core.resource.ZkIcons
import kotlin.reflect.KMutableProperty0

open class ZkBooleanField(
    context: ZkFieldContext,
    val prop: KMutableProperty0<Boolean>,
    val onChangeCallback : ((Boolean) -> Unit)? = null
) : ZkFieldBase<Boolean>(
    context = context,
    propName = prop.name
) {

    open val checkbox = ZkCheckBox(ZkIcons.check)

    override var readOnly: Boolean = context.readOnly
        set(value) {
            checkbox.disabled = value
            field = value
        }

    override fun buildFieldValue() {
        + div(context.styles.booleanField) {

            buildPoint.tabIndex = 0

            if (readOnly) checkbox.checkbox.readOnly = true

            checkbox.checked = prop.get()

            on(checkbox.checkbox, "change") {
               changeValue(checkbox.checked)
            }

            on(buildPoint, "keypress") {
                it as KeyboardEvent
                when (it.key) {
                    "Enter", " " -> {
                        checkbox.checked = ! checkbox.checked
                        changeValue(checkbox.checked)
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

    open fun changeValue(newValue : Boolean) {
        prop.set(newValue)
        touched = true
        onChange(newValue)
        context.validate()
    }

    open fun onChange(value : Boolean) {
        onChangeCallback?.invoke(value)
    }
}