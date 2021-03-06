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
import kotlinx.datetime.Instant
import org.w3c.dom.HTMLInputElement
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.zkFormStyles
import zakadabar.stack.frontend.resources.ZkFormatters.formatInstant
import zakadabar.stack.frontend.util.plusAssign
import kotlin.reflect.KMutableProperty0

open class ZkInstantField<T : BaseBo>(
    form: ZkForm<T>,
    val prop: KMutableProperty0<Instant>
) : ZkFieldBase<T, Instant>(
    form = form,
    propName = prop.name
) {

    private val input = document.createElement("input") as HTMLInputElement

    override var readOnly: Boolean = true

    override fun buildFieldValue() {
        input.classList += zkFormStyles.disabledString
        input.readOnly = true
        input.disabled = true
        input.value = formatInstant(prop.get())
        input.tabIndex = - 1
        + input
    }

    override fun onValidated(report: ValidityReport) {
        // instants are read only
    }

}