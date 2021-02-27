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
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.popup.alignPopup
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.minusAssign
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.launch

/**
 *
 */
abstract class ZkSelectBase<T : DtoBase, VT>(
    form: ZkForm<T>,
    propName: String,
    private val sortOptions: Boolean = true,
    private val options: suspend () -> List<Pair<VT, String>>
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

    private val selectedOption = ZkElement().withClass(ZkFormStyles.selectedOption)
    private val optionList = ZkElement().withClass(ZkFormStyles.selectOptionList)

    lateinit var items: List<Pair<VT, String>>

    override fun buildFieldValue() {
        launch {
            items = if (sortOptions) options().sortedBy { it.second } else options()
            render()
        }

        selectedOption.on("click") { _ ->
            optionList.toggle()
            if (optionList.isShown()) {
                alignPopup(optionList.element, selectedOption.element, ZkFormStyles.rowHeight * 5)
            }
        }

        optionList.on("click") { event ->
            event as MouseEvent

            val target = event.target
            if (target !is HTMLElement) return@on

            console.log(target, event)

            val entryId = target.dataset[DATASET_KEY] ?: return@on

            if (entryId == "0") {
                selectedOption.innerText = CoreStrings.notSelected
                setPropValue(null)
            } else {
                val value = entryId.toLong()
                val selected = items.first { it.first == value }
                selectedOption.innerText = selected.second
                setPropValue(selected)
            }

            optionList.hide()
        }

        val container = div(ZkFormStyles.selectContainer) {
            + selectedOption
            + optionList.hide()
        }

        container.tabIndex = 0

        on(container, "focus") { _ ->
            fieldBottomBorder.classList += ZkFormStyles.onFieldHover
        }

        on(container, "blur") { _ ->
            fieldBottomBorder.classList -= ZkFormStyles.onFieldHover
            optionList.hide()
        }

        on(container, "keydown") { event ->
            event as KeyboardEvent

            // FIXME add keyboard navigation to the list itself

            when (event.key) {
                "Enter", "ArrowDown" -> {
                    event.preventDefault()
                    if (optionList.isHidden()) {
                        optionList.show()
                    }
                }
                "Escape" -> {
                    event.preventDefault()
                    optionList.hide()
                }
            }
        }

        + container
    }

    fun render() {
        val value = getPropValue()

        var s = ""

        if (value == null || value == 0L) {
            s += """<div class="${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-${DATASET_KEY}="0">${CoreStrings.notSelected}</div>"""
            selectedOption.innerText = CoreStrings.notSelected
        } else {
            s += """<div class="${ZkFormStyles.selectEntry}" data-${DATASET_KEY}="0">${CoreStrings.notSelected}</div>"""
        }

//        for (i in 1..100) {
//            s += """<div class="${ZkFormStyles.selectEntry}" data-entry-id="0">${CoreStrings.notSelected} $i</div>"""
//        }

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

}