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
package zakadabar.samples.theperfectform.frontend.form

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.schema.ValidationRule
import zakadabar.stack.frontend.elements.ZkElement
import kotlin.reflect.KProperty0

class ValidatedString<T : RecordDto<T>>(
    private val form: ValidatedForm<T>,
    prop: KProperty0<String>
) : FormField<String>(
    element = document.createElement("input") as HTMLInputElement,
    prop = prop
) {

    private val input = element as HTMLInputElement

    override fun init(): ZkElement {
        if (readOnly) input.readOnly = true

        input.value = prop.get()

        on("input", form::validate)

        return this
    }


    override fun validated(fails: MutableList<ValidationRule<*>>?) {
        if (fails == null) {
            isValid = true
            return
        }

        element.style.backgroundColor = "red"
    }

    infix fun dependsOn(valami: Any) {
        this dependsOn 1
        this.dependsOn(1)
    }

}