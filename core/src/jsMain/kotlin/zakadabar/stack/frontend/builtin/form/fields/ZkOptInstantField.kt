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

import kotlinx.datetime.Instant
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.resources.ZkFormatters
import kotlin.reflect.KMutableProperty0

open class ZkOptInstantField<T : DtoBase>(
    form: ZkForm<T>,
    prop: KMutableProperty0<Instant?>
) : ZkStringBase<T, Instant?>(
    form = form,
    prop = prop,
    readOnly = true
) {

    override fun getPropValue() = prop.get()?.let { ZkFormatters.formatInstant(it) } ?: ""

    override fun setPropValue(value: String) {
        throw NotImplementedError("you should not change instants from the UI, read https://github.com/Kotlin/kotlinx-datetime#type-use-cases")
    }

}