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

open class ZkValueLocalDateField(
    context : ZkFieldContext,
    label: String,
    getter:() -> LocalDate,
    setter: (LocalDate) -> Unit = {}
) : ZkStringBaseV2<LocalDate,ZkValueLocalDateField>(
    context = context,
    label = label,
    getter = { getter().localized },
    setter = setter
) {

    override var valueOrNull : LocalDate?
        get() = input.value.toLocalDateOrNull
        set(value) {
            if (context.styles.useNativeDateInput) {
                input.value = value.toString()
            } else {
                input.value = value!!.localized
            }
            invalidInput = false
        }

    override fun onCreate() {
        super.onCreate()
        if (context.styles.useNativeDateInput) {
            input.type = "date"
        }
    }

    override fun setBackingValue(value: String) {
        val iv = input.value.toLocalDateOrNull

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