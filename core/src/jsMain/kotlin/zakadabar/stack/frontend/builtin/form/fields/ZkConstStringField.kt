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
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.util.PublicApi

/**
 * A field that displays a constant, readonly string.
 */
@PublicApi
open class ZkConstStringField<T : DtoBase>(
    form: ZkForm<T>,
    override var label: String,
    val value: String
) : ZkFieldBase<T, String>(
    form = form,
    propName = ""
) {
    private val input = document.createElement("input") as HTMLInputElement

    override fun buildFieldValue() {
        input.className = ZkFormStyles.recordId
        input.readOnly = true
        input.value = value
        input.tabIndex = - 1
        + input
    }
}