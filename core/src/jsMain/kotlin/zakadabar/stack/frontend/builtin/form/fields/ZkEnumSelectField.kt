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

import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import kotlin.reflect.KMutableProperty0

/**
 * An enum select form field.
 *
 * Create, Action and Query mode requires special care because in those modes the
 * DTO contains the first enum value which is not selected by the user but set
 * by the schema.
 */
open class ZkEnumSelectField<T : DtoBase, E : Enum<E>>(
    form: ZkForm<T>,
    val prop: KMutableProperty0<E>,
    val toEnum: (String) -> E,
    sortOptions: Boolean = true,
    options: suspend () -> List<Pair<E, String>>
) : ZkSelectBase<T, E>(form, prop.name, sortOptions, options) {

    var useShadow = form.mode in listOf(ZkElementMode.Create, ZkElementMode.Action, ZkElementMode.Query)

    var shadowValue: E? = null

    override fun fromString(string: String) = toEnum(string)

    override fun onResume() {
        super.onResume()
        invalidInput = (useShadow && shadowValue == null)
    }

    override fun getPropValue(): E? {
        return if (useShadow) {
            shadowValue
        } else {
            prop.get()
        }
    }

    override fun setPropValue(value: Pair<E, String>?) {

        shadowValue = value?.first

        if (value == null) {
            invalidInput = true
        } else {
            invalidInput = false
            prop.set(value.first)
        }

        form.validate()
    }

}