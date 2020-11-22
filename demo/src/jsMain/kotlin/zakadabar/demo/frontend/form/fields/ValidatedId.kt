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
package zakadabar.demo.frontend.form.fields

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.elements.ZkElement
import kotlin.reflect.KProperty0

class ValidatedId<T : RecordDto<T>>(
    private val prop: KProperty0<RecordId<T>>
) : FormField<RecordId<T>>(
    element = document.createElement("input") as HTMLElement
) {

    private val input = element as HTMLInputElement

    override fun init(): ZkElement {
        input.readOnly = true
        input.value = prop.get().toString()
        return this
    }

    override fun onValidated(report: ValidityReport) {
        // id is handled automatically, shouldn't be wrong
    }

}