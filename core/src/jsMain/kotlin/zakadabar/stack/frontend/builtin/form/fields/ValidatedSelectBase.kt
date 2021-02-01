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
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.FormClasses.Companion.formClasses
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch

abstract class ValidatedSelectBase<T : RecordDto<T>>(
    private val form: ZkForm<T>,
    private val sortOptions: Boolean = true,
    private val options: List<String>
) : FormField<String>(
    element = document.createElement("select") as HTMLElement
) {

    abstract fun getPropValue(): String?

    abstract fun setPropValue()

    abstract val propName: String

    override fun init(): ZkElement {
        className = formClasses.select

        launch {
            val items = if (sortOptions) options.sorted() else options
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

    fun render(items: List<String>) {
        val value = getPropValue()

        var s = if (value == null) {
            """<option value="" selected>${CoreStrings.notSelected}</option>"""
        } else {
            """<option value="">${CoreStrings.notSelected}</option>"""
        }

        items.forEach {
            s += if (it == value) {
                """<option value="$it" selected>${escape(it)}</option>"""
            } else {
                """<option value="$it">${escape(it)}</option>"""
            }
        }
        element.innerHTML = s
    }
}