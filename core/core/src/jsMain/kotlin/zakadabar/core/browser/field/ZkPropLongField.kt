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

open class ZkPropLongField(
    context : ZkFieldContext,
    var prop: KMutableProperty0<Long>
) : ZkStringBaseV2<Long,ZkPropLongField>(
    context = context,
    label = prop.name,
    getter = { prop.get().toString() }
) {

    override var valueOrNull : Long?
        get() = input.value.toLongOrNull()
        set(value) {
            prop.set(value!!)
            input.value = value.toString()
            invalidInput = false
        }

    override fun setBackingValue(value: String) {
        val iv = input.value.toLongOrNull()

        if (iv == null) {
            invalidInput = true
            context.validate()
        } else {
            invalidInput = false
            prop.set(iv)
            onUserChange(iv)
        }
    }

}