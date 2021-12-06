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

open class ZkValueOptStringField(
    context: ZkFieldContext,
    propName: String,
    getter: () -> String?,
    var setter: (String?) -> Unit = { }
) : ZkStringBaseV2<String?, ZkValueOptStringField>(
    context = context,
    label = propName,
    getter
) {

    override var valueOrNull : String?
        get() = input.value.ifEmpty { null }
        set(value) {
            getter = { value }
            input.value = value ?: ""
        }

    override fun setBackingValue(value: String) {
        val iv = value.ifEmpty { null }
        setter(iv)
        getter = { iv }
        onUserChange(iv)
    }
}
