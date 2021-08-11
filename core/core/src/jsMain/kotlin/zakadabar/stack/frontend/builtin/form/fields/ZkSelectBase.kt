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
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.zkFormStyles
import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.builtin.popup.alignPopup
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.resources.localizedStrings

abstract class ZkSelectBase<VT>(
    context: ZkFieldContext,
    propName: String,
    private val sortOptions: Boolean = true,
    private val options: suspend () -> List<Pair<VT, String>>,
    var onSelected: (Pair<VT, String>?) -> Unit = { }
) : ZkFieldBase<VT>(
    context = context,
    propName = propName
) {

    companion object {
        private const val DATASET_KEY = "value"
    }

    override var readOnly = context.readOnly
        set(value) {
            if (value) {
                container.firstElementChild?.classList?.plusAssign(zkFormStyles.disabledSelect)
                arrow.hide()
            } else {
                container.firstElementChild?.classList?.minusAssign(zkFormStyles.disabledSelect)
                arrow.show()
            }
            field = value
        }

    abstract fun fromString(string: String): VT

    abstract fun getPropValue(): VT?

    abstract fun setPropValue(value: Pair<VT, String>?)

    private lateinit var container: HTMLElement
    private lateinit var arrow: ZkElement
    private val selectedOption = ZkElement()
    private val optionList = ZkElement().css(ZkFormStyles.selectOptionList)

    lateinit var items: List<Pair<VT, String>>

    override fun onPause() {
        super.onPause()
        optionList.hide()
        optionList.element.remove()
    }

    override fun buildFieldValue() {
        io {
            items = if (sortOptions) options().sortedBy { it.second } else options()
            renderOptionList(getPropValue())
        }

        optionList.on("onmousedown") {
            it.preventDefault() // https://stackoverflow.com/a/57630197
        }

        optionList.on("click") {
            it as MouseEvent

            val target = it.target
            if (target !is HTMLElement) return@on

            val entryIdString = target.dataset[DATASET_KEY]
            val entryId = if (entryIdString.isNullOrEmpty()) null else fromString(entryIdString)
            val value = entryId?.let { items.firstOrNull { item -> item.first == entryId } }

            if (value == null) {
                selectedOption.innerText = localizedStrings.notSelected
            } else {
                selectedOption.innerText = value.second
            }

            touched = true
            setPropValue(value)
            onSelected(value)
            renderOptionList(value?.first) // FIXME this re-rendering is a bit too expensive I think
            optionList.hide()
        }

        container = div(ZkFormStyles.selectContainer) {
            + row(ZkFormStyles.selectedOption) {
                + selectedOption
                + ZkIcon(ZkIcons.arrowDropDown, size = 24).also { arrow = it }
                on("click") { toggleOptions() }
            }
        }

        container.tabIndex = 0
        optionList.element.tabIndex = 0

        optionList.on("blur") {
            optionList.element.remove()
            optionList.hide()
        }

        on(container, "keydown") {
            it as KeyboardEvent

            // FIXME add keyboard navigation to the list itself

            when (it.key) {
                "Enter", "ArrowDown" -> {
                    it.preventDefault()
                    if (optionList.isHidden()) {
                        toggleOptions()
                    }
                }
                "Escape" -> {
                    it.preventDefault()
                    optionList.element.remove()
                    optionList.hide()
                }
            }
        }

        if (readOnly) arrow.hide()

        optionList.hide()
        + container
    }

    open fun renderOptionList(value: VT?) {
        var s = ""

        if (value == null || value == 0L) {
            s += """<div class="${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-${DATASET_KEY}="">${localizedStrings.notSelected}</div>"""
        } else {
            s += """<div class="${ZkFormStyles.selectEntry}" data-${DATASET_KEY}="">${localizedStrings.notSelected}</div>"""
        }

        items.forEach {
            if (it.first == value) {
                s += """<div class=" ${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-${DATASET_KEY}="${it.first}">${escape(it.second)}</div>"""
            } else {
                s += """<div class="${ZkFormStyles.selectEntry}" data-${DATASET_KEY}="${it.first}">${escape(it.second)}</div>"""
            }
        }

        optionList.innerHTML = s
    }

    override fun focusValue() = toggleOptions()

    private fun toggleOptions() {
        if (readOnly) return

        optionList.toggle()

        if (optionList.isShown()) {
            document.body?.appendChild(optionList.element)
            alignPopup(optionList.element, selectedOption.element, zkFormStyles.rowHeight * 5)
            optionList.focus()
        } else {
            optionList.element.remove()
        }
    }
}