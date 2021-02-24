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

import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.util.dropdown.Dropdown
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.minusAssign
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch

abstract class ValidatedRecordSelectBase<T : DtoBase>(
    form: ZkForm<T>,
    propName: String,
    private val sortOptions: Boolean = true,
    private val options: suspend () -> List<Pair<RecordId<*>, String>>
) : FormField<T, String>(
    form = form,
    propName = propName
) {

    abstract fun getPropValue(): RecordId<*>?

    abstract fun setPropValue(value: Pair<RecordId<*>, String>?)

    private val selectedOption = ZkElement().withClass(ZkFormStyles.selectedOption)
    private val optionList = ZkElement().withClass(ZkFormStyles.selectOptionList)
    private val dropdown = Dropdown(optionList, selectedOption, "bottom")

    lateinit var items: List<Pair<RecordId<*>, String>>

    override fun buildFieldValue() {
        launch {
            items = if (sortOptions) options().sortedBy { it.second } else options()
            render()
        }

        on("focus") { _ ->
            fieldBottomBorder.classList += ZkFormStyles.onFieldHover
        }

        on("blur") { _ ->
            fieldBottomBorder.classList -= ZkFormStyles.onFieldHover
        }

        + dropdown

        optionList.on("click") { event ->
            event as MouseEvent

            val target = event.target
            if (target !is HTMLElement) return@on

            val entryId = target.dataset["entry-id"] ?: return@on

            println("entry-id: ${entryId}")

            if (entryId == "0") {
                selectedOption.innerText = CoreStrings.notSelected
                setPropValue(null)
            } else {
                val recordId = entryId.toLong()
                val selected = items.first { it.first == recordId }
                selectedOption.innerText = selected.second
                setPropValue(selected)
            }

            dropdown.close()
        }
    }

    fun render() {
        val value = getPropValue()

        var s = ""

        if (value == null || value == 0L) {
            s += """<div class="${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-entry-id="0">${CoreStrings.notSelected}</div>"""
            selectedOption.innerText = CoreStrings.notSelected
        } else {
            s += """<div class="${ZkFormStyles.selectEntry}" data-entry-id="0">${CoreStrings.notSelected}</div>"""
        }

        items.forEach {
            if (it.first == value) {
                s += """<div class=" ${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-entry-id="${it.first}">${escape(it.second)}</div>"""
                selectedOption.innerText = it.second
            } else {
                s += """<div class="${ZkFormStyles.selectEntry}" data-entry-id="${it.first}">${escape(it.second)}</div>"""
            }
        }

        optionList.innerHTML = s
    }

}