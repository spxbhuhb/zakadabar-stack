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
package zakadabar.core.browser.field

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.schema.ValidityReport
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty0

open class ZkEntityIdField<T : BaseBo>(
    context : ZkFieldContext,
    val prop: KProperty0<EntityId<T>>
) : ZkFieldBase<EntityId<T>,ZkEntityIdField<T>>(
    context = context,
    propName = prop.name
) {

    val input = document.createElement("input") as HTMLInputElement

    override var readOnly = context.readOnly || prop !is KMutableProperty<*>

    override var valueOrNull : EntityId<T>?
        get() = EntityId(input.value)
        set(value) {
            throw IllegalStateException("cannot set a read-only entity id")
        }

    override fun buildFieldValue() {
        input.classList += context.styles.disabledString
        input.disabled = true
        input.value = prop.get().toString()
        input.tabIndex = - 1
        + input
    }

    override fun onValidated(report: ValidityReport) {
        // id is handled automatically, shouldn't be wrong
    }

}