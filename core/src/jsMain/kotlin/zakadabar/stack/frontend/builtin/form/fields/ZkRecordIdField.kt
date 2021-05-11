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

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.zkFormStyles
import kotlin.reflect.KMutableProperty0

open class ZkRecordIdField<T : DtoBase>(
    form: ZkForm<T>,
    private val prop: KMutableProperty0<RecordId<T>>
) : ZkFieldBase<T, RecordId<T>>(
    form = form,
    propName = prop.name
) {

    private val input = document.createElement("input") as HTMLInputElement

    override var readOnly: Boolean = true

    override fun buildFieldValue() {
        input.className = zkFormStyles.disabledString
        input.disabled = true
        input.value = prop.get().toString()
        input.tabIndex = - 1
        + input
    }

    override fun onValidated(report: ValidityReport) {
        // id is handled automatically, shouldn't be wrong
    }

}