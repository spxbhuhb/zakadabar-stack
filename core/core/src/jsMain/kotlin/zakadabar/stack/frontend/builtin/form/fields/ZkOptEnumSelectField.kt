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

import kotlin.reflect.KMutableProperty0

open class ZkOptEnumSelectField<E : Enum<E>>(
    context : ZkFieldContext,
    val prop: KMutableProperty0<E?>,
    val toEnum: (String) -> E
) : ZkSelectBase<E>(context, prop.name) {

    override fun fromString(string: String) = toEnum(string)

    override fun getPropValue() = prop.get()

    override fun setPropValue(value: Pair<E, String>?) {
        prop.set(value?.first)
        context.validate()
    }

}