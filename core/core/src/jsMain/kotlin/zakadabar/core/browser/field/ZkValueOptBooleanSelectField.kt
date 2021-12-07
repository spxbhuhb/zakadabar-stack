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
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

@PublicApi
open class ZkValueOptBooleanSelectField(
    context : ZkFieldContext,
    label: String,
    getter: () -> Boolean?,
    setter: (Boolean?) -> Unit = {},
    renderer: SelectRenderer<Boolean?, ZkValueOptBooleanSelectField> = DropdownRenderer()
) : ZkSelectBaseV2<Boolean?, ZkValueOptBooleanSelectField>(
    context = context,
    label = label,
    renderer = renderer,
    getter = getter,
    setter = setter
) {

    override fun fromString(string: String) = when (string) {
        "true" -> true
        "false" -> false
        else -> throw IllegalStateException()
    }

    override fun setBackingValue(value: Pair<Boolean?, String>?, user : Boolean) {
        setter(value?.first)
        if (user) onUserChange(value?.first)
    }

}