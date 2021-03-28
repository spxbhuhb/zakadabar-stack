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
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.popup.alignPopup
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign

abstract class ZkSelectBase<T : DtoBase, VT>(
    form: ZkForm<T>,
    propName: String,
    private val sortOptions: Boolean = true,
    private val options: suspend () -> List<Pair<VT, String>>,
    var onSelected: (Pair<VT, String>?) -> Unit = { }
) : ZkFieldBase<T, VT>(
    form = form,
    propName = propName
) {

    companion object {
        private const val DATASET_KEY = "value"
    }

    abstract fun fromString(string: String): VT

    abstract fun getPropValue(): VT?

    abstract fun setPropValue(value: Pair<VT, String>?)

    private lateinit var container: HTMLElement
    private val selectedOption = ZkElement().css(ZkFormStyles.selectedOption)
    private val optionList = ZkElement().css(ZkFormStyles.selectOptionList)

    lateinit var items: List<Pair<VT, String>>

    override fun buildFieldValue() {
        io {
            items = if (sortOptions) options().sortedBy { it.second } else options()
            render(getPropValue())
        }

        selectedOption.on("click") { toggleOptions() }

        optionList.on("click") {
            it as MouseEvent

            val target = it.target
            if (target !is HTMLElement) return@on

            val entryIdString = target.dataset[DATASET_KEY]
            val entryId = if (entryIdString.isNullOrEmpty()) null else fromString(entryIdString)
            val value = entryId?.let { items.firstOrNull { item -> item.first == entryId } }

            touched = true
            setPropValue(value)
            onSelected(value)
            render(value?.first) // FIXME this re-rendering is a bit too expensive I think
            optionList.hide()
        }

        container = div(ZkFormStyles.selectContainer) {
            + selectedOption
            + optionList.hide()
        }

        container.tabIndex = 0

        on(container, "focus") {
            fieldBottomBorder.classList += ZkFormStyles.onFieldHover
        }

        on(container, "blur") {
            fieldBottomBorder.classList -= ZkFormStyles.onFieldHover
            optionList.hide()
        }

        on(container, "keydown") {
            it as KeyboardEvent

            // FIXME add keyboard navigation to the list itself

            when (it.key) {
                "Enter", "ArrowDown" -> {
                    it.preventDefault()
                    if (optionList.isHidden()) {
                        optionList.show()
                    }
                }
                "Escape" -> {
                    it.preventDefault()
                    optionList.hide()
                }
            }
        }

        + container
    }

    open fun render(value: VT?) {
        var s = ""

        if (value == null || value == 0L) {
            s += """<div class="${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-${DATASET_KEY}="">${strings.notSelected}</div>"""
            selectedOption.innerText = strings.notSelected
        } else {
            s += """<div class="${ZkFormStyles.selectEntry}" data-${DATASET_KEY}="">${strings.notSelected}</div>"""
        }

        items.forEach {
            if (it.first == value) {
                s += """<div class=" ${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-${DATASET_KEY}="${it.first}">${escape(it.second)}</div>"""
                selectedOption.innerText = it.second
            } else {
                s += """<div class="${ZkFormStyles.selectEntry}" data-${DATASET_KEY}="${it.first}">${escape(it.second)}</div>"""
            }
        }

        optionList.innerHTML = s
    }

    override fun focusValue() = toggleOptions()

    private fun toggleOptions() {
        optionList.toggle()
        if (optionList.isShown()) {
            alignPopup(optionList.element, selectedOption.element, ZkFormStyles.theme.form.rowHeight * 5)
            container.focus()
        }
    }
}