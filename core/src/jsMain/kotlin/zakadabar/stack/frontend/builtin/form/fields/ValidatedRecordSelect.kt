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
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSelectElement
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.FormClasses.Companion.formClasses
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch
import kotlin.reflect.KMutableProperty0

class ValidatedRecordSelect<T : RecordDto<T>>(
    private val form: ZkForm<T>,
    private val prop: KMutableProperty0<RecordId<*>>,
    private val sortOptions: Boolean = true,
    private val options: suspend () -> List<Pair<RecordId<*>, String>>
) : FormField<String>(
    element = document.createElement("select") as HTMLElement
) {

    override fun init(): ZkElement {
        className = formClasses.select

        launch {
            val items = if (sortOptions) options().sortedBy { it.second } else options()

            val value: Long

            if (prop.get() == 0L) {
                if (items.isEmpty()) {
                    value = 0L
                } else {
                    value = items[0].first
                    prop.set(value)
                }
            } else {
                value = prop.get()
            }

            var s = ""
            items.forEach {
                s += if (it.first == value) {
                    """<option value="${it.first}" selected>${escape(it.second)}</option>"""
                } else {
                    """<option value="${it.first}">${escape(it.second)}</option>"""
                }
            }

            element.innerHTML = s
        }

        on("input") { _ ->
            prop.set((element as HTMLSelectElement).value.toLongOrNull() ?: - 1)
            form.validate()
        }

        return this
    }

    override fun onValidated(report: ValidityReport) {
        val fails = report.fails[prop.name]
        if (fails == null) {
            isValid = true
            element.style.backgroundColor = "white"
        } else {
            isValid = false
            element.style.backgroundColor = "red"
        }
    }
}