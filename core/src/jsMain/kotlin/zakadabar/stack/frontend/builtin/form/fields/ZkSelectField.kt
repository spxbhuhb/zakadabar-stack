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
import zakadabar.stack.frontend.builtin.form.ZkForm
import kotlin.reflect.KMutableProperty0

open class ZkSelectField<T : DtoBase>(
    form: ZkForm<T>,
    val prop: KMutableProperty0<String>,
    sortOptions: Boolean = true,
    options: suspend () -> List<Pair<String, String>>
) : ZkSelectBase<T, String>(form, prop.name, sortOptions, options) {

    override fun fromString(string: String): String {
        return string
    }

    override fun getPropValue() = prop.get()

    override fun setPropValue(value: Pair<String, String>?) {
        if (value == null) return
        prop.set(value.first)
        form.validate()
    }

}