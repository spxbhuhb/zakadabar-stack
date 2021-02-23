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

import org.w3c.dom.HTMLSelectElement
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.form.ZkForm
import kotlin.reflect.KMutableProperty0

class ValidatedOptRecordSelect<T : DtoBase>(
    form: ZkForm<T>,
    val prop: KMutableProperty0<RecordId<*>?>,
    sortOptions: Boolean = true,
    options: suspend () -> List<Pair<RecordId<*>, String>>
) : ValidatedRecordSelectBase<T>(form, prop.name, sortOptions, options) {

    override fun getPropValue() = prop.get()

    override fun setPropValue() {
        val value = (element as HTMLSelectElement).value.toLongOrNull()
        if (value == null || value == 0L) {
            prop.set(null)
        } else {
            prop.set(value)
        }
    }

}