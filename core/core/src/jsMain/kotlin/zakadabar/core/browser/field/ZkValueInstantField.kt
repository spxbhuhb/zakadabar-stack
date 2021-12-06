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
import kotlinx.datetime.Instant
import org.w3c.dom.HTMLInputElement
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.localized
import zakadabar.core.schema.ValidityReport
import kotlin.reflect.KMutableProperty0

open class ZkValueInstantField(
    context: ZkFieldContext,
    label : String,
    var getter:() -> Instant
) : ZkFieldBase<Instant,ZkValueInstantField>(
    context = context,
    propName = label
) {

    val input = document.createElement("input") as HTMLInputElement

    override var readOnly: Boolean = true

    override var valueOrNull : Instant?
        get() = getter()
        set(value) {
           throw IllegalStateException("instants cannot be changed")
        }

    override fun buildFieldValue() {
        input.classList += context.styles.disabledString
        input.readOnly = true
        input.disabled = true
        input.value = getter().localized
        input.tabIndex = - 1
        + input
    }

    override fun onValidated(report: ValidityReport) {
        // instants are read only
    }

}