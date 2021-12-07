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

open class ZkValueOptEnumSelectField<E : Enum<E>>(
    context : ZkFieldContext,
    label: String,
    getter: () -> E?,
    setter: (E?) -> Unit = {},
    renderer : SelectRenderer<E?,ZkValueOptEnumSelectField<E>> = DropdownRenderer(),
    val toEnum: (String) -> E?
) : ZkSelectBaseV2<E?,ZkValueOptEnumSelectField<E>>(
    context = context,
    label = label,
    renderer = renderer,
    getter = getter
) {

    override fun fromString(string: String) = toEnum(string)

    override fun setBackingValue(value: Pair<E?, String>?, user : Boolean) {
        setter(value?.first)
        if (user) onUserChange(value?.first)
    }

}