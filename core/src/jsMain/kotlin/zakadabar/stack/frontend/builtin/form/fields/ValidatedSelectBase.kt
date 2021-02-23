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
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch

abstract class ValidatedSelectBase<T : DtoBase>(
    form: ZkForm<T>,
    propName: String,
    private val sortOptions: Boolean = true,
    private val options: List<String>
) : FormField<T, String>(
    form = form,
    propName = propName
) {

    abstract fun getPropValue(): String?

    abstract fun setPropValue()

    val select = document.createElement("select") as HTMLElement

    override fun buildFieldValue() {
        select.className = ZkFormStyles.select

        launch {
            val items = if (sortOptions) options.sorted() else options
            render(items)
        }

        on(select, "input") { _ ->
            setPropValue()
            form.validate()
        }

        + select
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
        select.innerHTML = s
    }
}