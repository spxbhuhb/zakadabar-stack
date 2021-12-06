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

import kotlinx.datetime.LocalDate
import zakadabar.core.resource.localized
import zakadabar.core.resource.toLocalDateOrNull
import kotlin.reflect.KMutableProperty0

open class ZkValueOptLocalDateField(
    context : ZkFieldContext,
    label: String,
    getter:() -> LocalDate?,
    var setter: (LocalDate?) -> Unit = {}
) : ZkStringBaseV2<LocalDate?,ZkValueOptLocalDateField>(
    context = context,
    label = label,
    getter = { getter()?.localized }
) {

    override var valueOrNull : LocalDate?
        get() = input.value.toLocalDateOrNull
        set(value) {
            input.value = value!!.localized
            invalidInput = false
        }

    override fun setBackingValue(value: String) {
        val iv = input.value.toLocalDateOrNull

        if (iv == null && input.value.isNotEmpty()) {
            invalidInput = true
            context.validate()
        } else {
            invalidInput = false
            setter(iv)
            onUserChange(iv)
        }
    }
}