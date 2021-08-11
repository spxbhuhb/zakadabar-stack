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

import kotlinx.datetime.LocalDateTime
import zakadabar.stack.resources.toLocalDateTimeOrNull
import kotlin.reflect.KMutableProperty0

open class ZkLocalDateTimeField(
    context : ZkFieldContext,
    prop: KMutableProperty0<LocalDateTime>
) : ZkStringBase<LocalDateTime>(
    context = context,
    prop = prop
) {

    override fun getPropValue() = prop.get().toString()

    override fun setPropValue(value: String) {
        val iv = input.value.toLocalDateTimeOrNull

        if (iv == null) {
            invalidInput = true
        } else {
            invalidInput = false
            prop.set(iv)
        }
    }

}