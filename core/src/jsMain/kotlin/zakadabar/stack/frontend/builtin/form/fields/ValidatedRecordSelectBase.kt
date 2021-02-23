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
import org.w3c.dom.HTMLSelectElement
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.elements.minusAssign
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch

abstract class ValidatedRecordSelectBase<T : DtoBase>(
    form: ZkForm<T>,
    propName: String,
    private val sortOptions: Boolean = true,
    private val options: suspend () -> List<Pair<RecordId<*>, String>>
) : FormField<T, String>(
    form = form,
    propName = propName
) {

    abstract fun getPropValue(): RecordId<*>?

    abstract fun setPropValue()

    val select = document.createElement("select") as HTMLSelectElement

    override fun buildFieldValue() {
        select.className = ZkFormStyles.select

        launch {
            val items = if (sortOptions) options().sortedBy { it.second } else options()
            render(items)
        }

        on(select, "input") { _ ->
            setPropValue()
            form.validate()
        }

        on(select, "focus") { _ ->
            fieldBottomBorder.classList += ZkFormStyles.onFieldHover
        }

        on(select, "blur") { _ ->
            fieldBottomBorder.classList -= ZkFormStyles.onFieldHover
        }

        + select
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
        select.innerHTML = s
    }
}