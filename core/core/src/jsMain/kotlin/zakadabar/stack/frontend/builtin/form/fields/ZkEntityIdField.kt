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
package zakadabar.core.frontend.builtin.form.fields

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.core.data.BaseBo
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.schema.ValidityReport
import zakadabar.core.frontend.builtin.form.zkFormStyles
import zakadabar.core.frontend.util.plusAssign
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty0

open class ZkEntityIdField<T : BaseBo>(
    context : ZkFieldContext,
    private val prop: KProperty0<EntityId<T>>
) : ZkFieldBase<EntityId<T>>(
    context = context,
    propName = prop.name
) {

    private val input = document.createElement("input") as HTMLInputElement

    override var readOnly = context.readOnly || prop !is KMutableProperty<*>

    override fun buildFieldValue() {
        input.classList += zkFormStyles.disabledString
        input.disabled = true
        input.value = prop.get().toString()
        input.tabIndex = - 1
        + input
    }

    override fun onValidated(report: ValidityReport) {
        // id is handled automatically, shouldn't be wrong
    }

}