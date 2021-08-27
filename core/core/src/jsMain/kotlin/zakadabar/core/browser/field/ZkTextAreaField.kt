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
import org.w3c.dom.HTMLTextAreaElement
import zakadabar.core.browser.util.plusAssign
import kotlin.reflect.KMutableProperty0

open class ZkTextAreaField(
    context : ZkFieldContext,
    private val prop: KMutableProperty0<String>
) : ZkFieldBase<String,ZkTextAreaField>(
    context = context,
    propName = prop.name
) {

    open val area = document.createElement("textarea") as HTMLTextAreaElement

    override var valueOrNull : String?
        get() = area.value
        set(value) {
            prop.set(value!!)
            area.value = value
        }

    override var readOnly = context.readOnly
        set(value) {
            area.disabled = value
            field = value
        }

    @Suppress("DuplicatedCode") // i don't want to mix this with string field
    override fun buildFieldValue() {
        buildPoint.style.flexGrow = "1"
        buildPoint.style.display = "flex"

        area.classList += context.styles.textarea

        if (readOnly) {
            area.disabled = true
        }

        area.value = prop.get()

        on(area, "input") {
            touched = true
            prop.set(area.value)
            onUserChange(area.value)
        }

        focusEvents(area)

        + area
    }

    override fun focusValue() {
        area.focus()
    }

    override fun needsMandatoryMark() = stringMandatoryMark()

}