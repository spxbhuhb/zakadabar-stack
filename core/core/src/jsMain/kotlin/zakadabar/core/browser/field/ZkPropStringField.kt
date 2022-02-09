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

import kotlin.reflect.KMutableProperty0

open class ZkPropStringField(
    context: ZkFieldContext,
    var prop: KMutableProperty0<String>
) : ZkStringBaseV2<String, ZkPropStringField>(
    context = context,
    propName = prop.name,
    getter = { prop.get() },
) {

    override var valueOrNull: String?
        get() = input.value
        set(value) {
            prop.set(value !!)
            input.value = value
        }

    override fun setBackingValue(value: String) {
        prop.set(value)
        onUserChange(value)
    }

    override fun needsMandatoryMark() = stringMandatoryMark()

}