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
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.FormClasses.Companion.formClasses
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch

abstract class ValidatedRecordSelectBase<T : DtoBase>(
    private val form: ZkForm<T>,
    private val sortOptions: Boolean = true,
    private val options: suspend () -> List<Pair<RecordId<*>, String>>
) : FormField<String>(
    element = document.createElement("select") as HTMLElement
) {

    abstract fun getPropValue(): RecordId<*>?

    abstract fun setPropValue()

    abstract val propName: String

    override fun init(): ZkElement {
        className = formClasses.select

        launch {
            val items = if (sortOptions) options().sortedBy { it.second } else options()
            render(items)
        }

        on("input") { _ ->
            setPropValue()
            form.validate()
        }

        return this
    }

    override fun onValidated(report: ValidityReport) {
        val fails = report.fails[propName]
        if (fails == null) {
            isValid = true
            element.style.backgroundColor = "white"
        } else {
            isValid = false
            element.style.backgroundColor = "red"
        }
    }

    fun render(items: List<Pair<RecordId<*>, String>>) {
        val value = getPropValue()

        var s = if (value == null || value == 0L) {
            """<option value="0" selected>${CoreStrings.notSelected}</option>"""
        } else {
            """<option value="0">${CoreStrings.notSelected}</option>"""
        }

        items.forEach {
            s += if (it.first == value) {
                """<option value="${it.first}" selected>${escape(it.second)}</option>"""
            } else {
                """<option value="${it.first}">${escape(it.second)}</option>"""
            }
        }
        element.innerHTML = s
    }
}