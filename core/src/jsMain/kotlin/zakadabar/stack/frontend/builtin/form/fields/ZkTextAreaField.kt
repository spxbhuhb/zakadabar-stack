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
import org.w3c.dom.HTMLTextAreaElement
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import kotlin.reflect.KMutableProperty0

open class ZkTextAreaField<T : DtoBase>(
    form: ZkForm<T>,
    private val prop: KMutableProperty0<String>
) : ZkFieldBase<T, String>(
    form = form,
    propName = prop.name
) {

    private val area = document.createElement("textarea") as HTMLTextAreaElement

    override fun buildFieldValue() {
        buildElement.style.flexGrow = "1"
        buildElement.style.display = "flex"

        area.className = ZkFormStyles.textarea
        area.style.flexGrow = "1"
        area.style.resize = "none"

        if (readOnly) area.readOnly = true

        area.value = prop.get()

        on(area, "input") { _ ->
            prop.set(area.value)
            form.validate()
        }

        + area
    }

    override fun focusValue() {
        area.focus()
    }

}