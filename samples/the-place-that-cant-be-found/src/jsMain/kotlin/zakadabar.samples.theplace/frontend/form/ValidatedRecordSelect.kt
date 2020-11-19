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
package zakadabar.samples.theplace.frontend.form

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.dom.set
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.schema.ValidationRule
import zakadabar.stack.frontend.builtin.util.dropdown.Dropdown
import zakadabar.stack.frontend.elements.ZkElement
import kotlin.reflect.KProperty0

class ValidatedRecordSelect<T : RecordDto<T>, E : Any>(
    prop: KProperty0<String>,
    private val form: ValidatedForm<T>,
    private val query: QueryDto<E>,
    private val entryBuilder: (E) -> ZkElement
) : FormField<String>(
    element = document.createElement("div") as HTMLElement,
    prop = prop
) {

    private val selectedEntry = ZkElement()
    private val entryList = buildNew { + "betoltes" }

    override fun init(): ZkElement {

        this += Dropdown(entryList, selectedEntry, "bottom")



        return this
    }

    override fun onValidated(fails: MutableList<ValidationRule<*>>?) {
        if (fails == null) {
            isValid = true
            return
        }

        element.style.backgroundColor = "red"
    }
}

open class ItemList<I>(
    private val query: QueryDto<I>,
    private val entryBuilder: (I) -> ZkElement,
    private val onSelected : (I) -> Unit
) : ZkElement() {

    private lateinit var entries: List<I>

    override fun init(): ItemList<I> {

        launchBuild {
            entries = query.execute()

            clearChildren()

            var index = 0

            entries.forEach {
                val item = entryBuilder(it)
                item.element.dataset["zk-select-index"] = "${index++}"
            }
        }

        on("click", ::onClick)

        return this
    }

    private fun onClick(event : Event) {
        var current = event.target as? HTMLElement
        while (current != null) {
            val index = current.dataset["zk-select-index"]?.toIntOrNull()
            if (index != null) {
                onSelected(entries[index])
                return
            }
            current = current.parentElement as HTMLElement
        }
    }

}