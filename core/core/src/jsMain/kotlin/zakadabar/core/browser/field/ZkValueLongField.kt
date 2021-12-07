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

open class ZkValueLongField(
    context : ZkFieldContext,
    label : String,
    getter:() -> Long,
    setter: (Long) -> Unit = {}
) : ZkStringBaseV2<Long,ZkValueLongField>(
    context = context,
    label = label,
    getter = { getter().toString() },
    setter = setter
) {

    override var valueOrNull : Long?
        get() = input.value.toLongOrNull()
        set(value) {
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
            setter(iv)
            onUserChange(iv)
        }
    }

}