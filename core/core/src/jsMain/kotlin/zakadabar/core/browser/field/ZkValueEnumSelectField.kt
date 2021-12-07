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

import zakadabar.core.browser.field.select.DropdownRenderer
import zakadabar.core.browser.field.select.SelectRenderer
import kotlin.reflect.KMutableProperty0

/**
 * An enum select form field.
 *
 * Create, Action and Query mode requires special care because in those modes the
 * BO contains the first enum value which is not selected by the user but set
 * by the schema.
 */
open class ZkValueEnumSelectField<E : Enum<E>>(
    context : ZkFieldContext,
    label: String,
    getter: () -> E,
    setter: (E) -> Unit = {},
    renderer: SelectRenderer<E,ZkValueEnumSelectField<E>> = DropdownRenderer(),
    val toEnum: (String) -> E
) : ZkSelectBaseV2<E,ZkValueEnumSelectField<E>>(
    context = context,
    label = label,
    renderer = renderer,
    getter = getter
) {

    var shadowValue: E? = null

    override fun fromString(string: String) = toEnum(string)

    override fun onResume() {
        super.onResume()
        invalidInput = (context.useShadow && shadowValue == null)
    }

    override var getter = {
       if (context.useShadow) {
            shadowValue
        } else {
            getter()
        }
    }

    override fun setBackingValue(value: Pair<E, String>?, user : Boolean) {

        shadowValue = value?.first

        if (value == null) {
            invalidInput = true
            if (user) context.validate()
        } else {
            invalidInput = false
            setter(value.first)
            if (user) onUserChange(value.first)
        }
    }

}