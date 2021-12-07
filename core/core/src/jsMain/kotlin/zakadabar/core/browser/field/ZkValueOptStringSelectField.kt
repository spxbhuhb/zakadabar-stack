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

open class ZkValueOptStringSelectField(
    context: ZkFieldContext,
    label: String,
    getter: () -> String?,
    setter: (String?) -> Unit = {},
    renderer: SelectRenderer<String?, ZkValueOptStringSelectField> = DropdownRenderer()
) : ZkSelectBaseV2<String?, ZkValueOptStringSelectField>(
    context = context,
    label = label,
    renderer = renderer,
    getter = getter,
    setter = setter
) {

    override fun fromString(string: String): String {
        return string
    }

    override fun setBackingValue(value: Pair<String?, String>?, user: Boolean) {
        val iv = value?.first
        setter(iv)
        if (user) onUserChange(iv)
    }

}